package http;

import util.Encoding;
import util.Strings;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class PlainTextResponse extends HttpResponse {

    private final String body;
    private final Encoding encoding;

    public PlainTextResponse(HttpRequest request) {
        super(request);

        this.body = switch (request.getPath()) {
            case String p when p.startsWith("/echo/") -> Strings.after(p, "/echo/");
            case String p when "/user-agent".equals(p) && request.getHeaders().containsKey(HttpHeader.USER_AGENT) ->
                    request.getHeaders().get(HttpHeader.USER_AGENT);
            case null, default -> "";
        };

        this.encoding = Encoding.parseEncoding(request.getHeaders().get(HttpHeader.ACCEPT_ENCODING));
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.OK;
    }

    @Override
    protected Map<HttpHeader, String> getResponseHeaders() {
        Map<HttpHeader, String> headers = new HashMap<>();
        headers.put(HttpHeader.CONTENT_TYPE, "text/plain");
        headers.put(HttpHeader.CONTENT_LENGTH, String.valueOf(body.length()));
        if (Objects.nonNull(encoding)) {
            headers.put(HttpHeader.CONTENT_ENCODING, encoding.getEncoding());
        }
        return Map.copyOf(headers);
    }

    @Override
    public byte[] body() {
        return body.getBytes(StandardCharsets.UTF_8);
    }

}
