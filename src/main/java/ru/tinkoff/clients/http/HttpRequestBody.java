package ru.tinkoff.clients.http;

import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.core.MultiMap;
import io.vertx.mutiny.core.buffer.Buffer;
import io.vertx.mutiny.ext.web.client.HttpRequest;
import io.vertx.mutiny.ext.web.client.HttpResponse;

public interface HttpRequestBody {
    HttpRequestBody EMPTY_BODY = new Noop();

    Uni<HttpResponse<Buffer>> send(HttpRequest<Buffer> request);

    class Noop implements HttpRequestBody {
        private Noop() {
        }

        @Override
        public Uni<HttpResponse<Buffer>> send(HttpRequest<Buffer> request) {
            return request.send();
        }
    }

    class Raw implements HttpRequestBody {
        private final Buffer buffer;

        public Raw(Buffer buffer) {
            this.buffer = buffer;
        }

        public static Raw of(String body) {
            return new Raw(Buffer.buffer(body));
        }

        public static Raw of(Buffer body) {
            return new Raw(body);
        }

        @Override
        public Uni<HttpResponse<Buffer>> send(HttpRequest<Buffer> request) {
            return request.sendBuffer(buffer);
        }
    }

    class Json implements HttpRequestBody {
        private final Object json;

        public Json(Object json) {
            this.json = json;
        }

        public static Json of(Object body) {
            return new Json(body);
        }

        @Override
        public Uni<HttpResponse<Buffer>> send(HttpRequest<Buffer> request) {
            return request.sendJson(json);
        }
    }

    class Form implements HttpRequestBody {

        public final MultiMap form;

        public Form(MultiMap body) {
            this.form = body;
        }

        public static Form of(MultiMap body) {
            return new Form(body);
        }

        @Override
        public Uni<HttpResponse<Buffer>> send(HttpRequest<Buffer> request) {
            return request.sendForm(form);
        }
    }
}
