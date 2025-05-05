package http;

import config.Environment;
import util.Strings;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public abstract class HttpResponse {

    private final HttpRequest request;

    public HttpResponse(HttpRequest request) {
        this.request = request;
    }

    protected abstract HttpStatus getResponseStatus();

    protected Map<HttpHeader, String> getResponseHeaders() {
        return Optional.ofNullable(request.getHeaders().get(HttpHeader.CONNECTION))
                .map(connection -> Map.of(HttpHeader.CONNECTION, connection))
                .orElse(Map.of());
    }

    public byte[] status() {
        return "%s%s%s%s"
                .formatted(
                        Environment.HTTP_VERSION,
                        Strings.SPACE,
                        getResponseStatus(),
                        Strings.CRLF)
                .getBytes(StandardCharsets.UTF_8);
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
