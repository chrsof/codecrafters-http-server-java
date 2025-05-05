package http;

public final class HttpResponseFactory {

    public static HttpResponse getResponse(HttpRequest request) {
        return switch (request.getPath()) {
            case null -> new NoBodyResponse(request);
            case "/" -> new NoBodyResponse(request);
            case String p when p.startsWith("/echo/") -> new PlainTextResponse(request);
            case String p when ("/user-agent".equals(p) && request.getHeaders().containsKey(HttpHeader.USER_AGENT)) ->
                    new PlainTextResponse(request);
            case String p when p.startsWith("/files/") ->
                    request.getMethod() == HttpMethod.POST ? new WriteFileResponse(request) : new ReadFileResponse(request);
            default -> new NotFoundResponse(request);
        };
    }

}
