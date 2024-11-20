package http;

import java.util.Map;

public class NotFoundResponse extends HttpResponse {

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    protected Map<HttpHeader, String> getResponseHeaders() {
        return Map.of();
    }

    @Override
    public byte[] body() {
        return new byte[0];
    }

}
