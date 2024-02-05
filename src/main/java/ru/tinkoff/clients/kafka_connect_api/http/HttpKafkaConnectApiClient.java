package ru.tinkoff.clients.kafka_connect_api.http;

import io.vertx.core.http.HttpMethod;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.UriBuilder;
import ru.tinkoff.clients.http.HttpClient;
import ru.tinkoff.clients.http.HttpClientUtils;
import ru.tinkoff.clients.http.HttpRequestBody;
import ru.tinkoff.clients.kafka_connect_api.KafkaConnectApiClient;
import ru.tinkoff.clients.kafka_connect_api.config.KafkaConnectApiClientConfig;
import ru.tinkoff.clients.kafka_connect_api.model.ConnectorPlugin;

import java.util.Arrays;
import java.util.List;
@ApplicationScoped
public class HttpKafkaConnectApiClient implements KafkaConnectApiClient {

    private final HttpClient httpClient;

    private final KafkaConnectApiClientConfig config;

    private UriBuilder connectorPluginsUri;

    public HttpKafkaConnectApiClient(HttpClient httpClient, KafkaConnectApiClientConfig config) {
        this.httpClient = httpClient;
        this.config = config;
    }
    @PostConstruct
    public void setup() {
        var pluginsPath = String.format("%s/connector-plugins", config.kafkaConnectApiUrl());

        connectorPluginsUri = UriBuilder.fromPath(pluginsPath);
    }
    @Override
    public List<ConnectorPlugin> listConnectorPlugins() {
        var httpRequest = httpClient.sendRequest(HttpMethod.GET, connectorPluginsUri.build(),
            HttpRequestBody.EMPTY_BODY, config.requestTimeout());

        var httpResponse = httpClient.retryableRunBlocking("validate", httpRequest, RuntimeException::new);

        if (HttpClientUtils.isSuccess(httpResponse)) {
            var result = httpResponse.bodyAsJson(ConnectorPlugin[].class);
            return Arrays.stream(result).toList();
        } else {
            throw new RuntimeException("Failed to list connector plugins");
        }
    }
}
