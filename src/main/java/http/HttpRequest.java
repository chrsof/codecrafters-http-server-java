package http;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    private final HttpMethod method;
    private final String path;
    private Map<HttpHeader, String> headers;
    private final String body;

    public HttpRequest(HttpRequest.Builder builder) {
        this.method = builder.method;
        this.path = builder.path;
        this.headers = Map.copyOf(builder.headers);
        this.body = builder.body;
    }

    public String getPath() {
        return path;
    }

    public Map<HttpHeader, String> getHeaders() {
        return headers;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private HttpMethod method;
        private String path;
        private final Map<HttpHeader, String> headers;
        private String body;

        public Builder() {
            headers = new HashMap<>();
        }

        public Builder forMethod(HttpMethod method) {
            this.method = method;
            return this;
        }

        public Builder withPath(String path) {
            this.path = path;
            return this;
        }

        public Builder addHeader(HttpHeader header, String value) {
            headers.putIfAbsent(header, value);
            return this;
        }

        public Builder withBody(String body) {
            this.body = body;
            return this;
        }

        public HttpRequest build() {
            return new HttpRequest(this);
        }
    }

}
