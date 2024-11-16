package http;

import util.Strings;

import java.nio.charset.StandardCharsets;
import java.util.Map;

public class PlainTextResponse extends HttpResponse {

    private final String body;

    public PlainTextResponse(HttpRequest request) {
        super(request);

        this.body = switch (request.getPath()) {
            case String p when p.startsWith("/echo/") -> Strings.after(p, "/echo/");
            case String p when "/user-agent".equals(p) && request.getHeaders().containsKey(HttpHeader.USER_AGENT) ->
                    request.getHeaders().get(HttpHeader.USER_AGENT);
            case null, default -> "";
        };
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.OK;
    }

    @Override
    protected Map<HttpHeader, String> getResponseHeaders() {
        return Map.of(
                HttpHeader.CONTENT_TYPE, "text/plain",
                HttpHeader.CONTENT_LENGTH, String.valueOf(body.length()));
    }

    @Override
    public byte[] body() {
        return body.getBytes(StandardCharsets.UTF_8);
    }

}
