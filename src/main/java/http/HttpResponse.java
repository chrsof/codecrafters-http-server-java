package http;

import config.Environment;
import util.Strings;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public abstract class HttpResponse {

    private final HttpRequest request;

    protected abstract HttpStatus getResponseStatus();

    protected abstract Map<HttpHeader, String> getResponseHeaders();

    public HttpResponse(HttpRequest request) {
        this.request = request;
    }

    public byte[] status() {
        return "%s %s%s".formatted(Environment.HTTP_VERSION, getResponseStatus(), Strings.CRLF).getBytes(StandardCharsets.UTF_8);
    }

    public byte[] headers() {
        StringBuilder builder = new StringBuilder();
        getResponseHeaders().forEach((header, value) -> builder
                .append(header)
                .append(Strings.COLON)
                .append(Strings.SPACE)
                .append(value)
                .append(Strings.CRLF)
        );
        builder.append(Strings.CRLF);
        return builder.toString().getBytes(StandardCharsets.UTF_8);
    }

    public abstract byte[] body();

}
