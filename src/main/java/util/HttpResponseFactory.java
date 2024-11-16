package util;

import http.*;

public final class HttpResponseFactory {

    private HttpResponseFactory() {
    }

    public static HttpResponse getResponse(HttpRequest request) {
        return switch (request.getPath()) {
            case null -> new NoBodyResponse(request);
            case "/" -> new NoBodyResponse(request);
            case String p when p.startsWith("/echo/") || ("/user-agent".equals(p) && request.getHeaders().containsKey(HttpHeader.USER_AGENT)) ->
                    new PlainTextResponse(request);
            case String p when p.startsWith("/files/") -> new FileResponse(request);
            default -> new NotFoundResponse(request);
        };
    }

}
