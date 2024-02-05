package ru.tinkoff.clients.http;

import io.smallrye.mutiny.Uni;
import io.vertx.core.http.HttpMethod;
import io.vertx.mutiny.core.Vertx;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpRequest;
import io.vertx.mutiny.ext.web.client.HttpResponse;
import io.vertx.mutiny.ext.web.client.WebClient;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.jboss.logging.Logger;

import java.net.URI;
import java.time.Duration;
import java.util.function.Function;

@ApplicationScoped
public class HttpClient {
    private final Logger logger;
    private final WebClient client;

    @ConfigProperty(name = "enable.authorization", defaultValue = "true")
    boolean authorizationEnabled;

    @ConfigProperty(name = "http-client.request-total-await-timeout")
    Duration requestTotalAwaitTimeout;

    @ConfigProperty(name = "http-client.initial-retry-backoff")
    Duration initialRetryBackoff;

    @ConfigProperty(name = "http-client.default-retries")
    int defaultRetries;

    @Inject
    public HttpClient(Logger logger, Vertx vertx) {
        this.logger = logger;
        this.client = WebClient.create(vertx);
    }

    public Uni<HttpResponse<Buffer>> sendRequest(HttpMethod httpMethod, URI absoluteUri,
                                                 HttpRequestBody requestBody, Duration timeout) {
        logger.debugf("Send `%s` request: %s", httpMethod.name(), absoluteUri.toString());
        return requestBody
                .send(client.requestAbs(httpMethod, absoluteUri.toString()).timeout(timeout.toMillis()))
                .eventually(() -> logger.debugf("Finish `%s` request: %s", httpMethod.name(),
                        absoluteUri.toString()));
    }

    public Uni<HttpResponse<Buffer>> sendRequest(HttpMethod httpMethod, URI absoluteUri,
                                                 HttpRequestBody requestBody,
                                                 Function<HttpRequest<Buffer>, HttpRequest<Buffer>> customizeRequest, Duration timeout) {
        logger.debugf("Send `%s` request: %s", httpMethod.name(), absoluteUri.toString());

        var request = client.requestAbs(httpMethod, absoluteUri.toString()).timeout(timeout.toMillis());
        var customizedRequest = customizeRequest.apply(request);

        return requestBody.send(customizedRequest).eventually(
                () -> logger.debugf("Finish `%s` request: %s", httpMethod.name(), absoluteUri.toString()));
    }

    public Uni<HttpResponse<Buffer>> sendAuthenticatedRequest(HttpMethod httpMethod, URI absoluteUri,
                                                              HttpRequestBody requestBody, Duration timeout, String token) {
        if (authorizationEnabled) {
            return sendRequest(httpMethod, absoluteUri, requestBody,
                    r -> r.bearerTokenAuthentication(token), timeout);
        }

        return sendRequest(httpMethod, absoluteUri, requestBody, timeout);
    }

    public Uni<HttpResponse<Buffer>> sendAuthenticatedRequest(HttpMethod httpMethod, URI absoluteUri,
                                                              HttpRequestBody requestBody,
                                                              Function<HttpRequest<Buffer>, HttpRequest<Buffer>> customizeRequest, Duration timeout,
                                                              String token) {
        if (authorizationEnabled) {
            return sendRequest(httpMethod, absoluteUri, requestBody,
                    customizeRequest.andThen(r -> r.bearerTokenAuthentication(token)), timeout);
        }

        return sendRequest(httpMethod, absoluteUri, requestBody, customizeRequest, timeout);
    }

    public <E extends RuntimeException> Uni<HttpResponse<Buffer>> retryableRun(String method,
                                                                               Uni<HttpResponse<Buffer>> request, Function<Throwable, E> mapError) {
        return retryableRun(method, request, mapError, defaultRetries, initialRetryBackoff);
    }

    public <E extends RuntimeException> Uni<HttpResponse<Buffer>> retryableRun(String method,
                                                                               Uni<HttpResponse<Buffer>> request, Function<Throwable, E> mapError, int retries,
                                                                               Duration retryBackOff) {
        try {
            return request.onFailure().retry().withBackOff(retryBackOff).atMost(retries);
        } catch (Throwable e) {
            logger.debugf(e, "Error happened while processing request: %s", method);
            return Uni.createFrom().failure(mapError.apply(e));
        }
    }

    public <E extends RuntimeException> HttpResponse<Buffer> retryableRunBlocking(String method,
                                                                                  Uni<HttpResponse<Buffer>> request, Function<Throwable, E> mapError) {
        return retryableRunBlocking(method, request, mapError, defaultRetries, initialRetryBackoff,
                requestTotalAwaitTimeout);
    }

    public <E extends RuntimeException> HttpResponse<Buffer> retryableRunBlocking(String method,
                                                                                  Uni<HttpResponse<Buffer>> request, Function<Throwable, E> mapError, int retries,
                                                                                  Duration retryBackoff, Duration awaitResponse) {
        try {
            return request.onFailure().retry().withBackOff(retryBackoff).atMost(retries).await()
                    .atMost(awaitResponse);
        } catch (Throwable e) {
            logger.debugf(e, "Error happened while processing request `%s`", method);
            throw mapError.apply(e);
        }
    }
}
