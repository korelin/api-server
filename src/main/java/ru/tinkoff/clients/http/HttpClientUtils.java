package ru.tinkoff.clients.http;

import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpResponse;

public class HttpClientUtils {

    public static boolean isSuccess(HttpResponse<Buffer> httpResponse) {
        return httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300;
    }

    public static boolean isNotFound(HttpResponse<Buffer> httpResponse) {
        return httpResponse.statusCode() == 404;
    }

    public static boolean isNotReady(HttpResponse<Buffer> httpResponse) {
        return httpResponse.statusCode() == 412;
    }
}
