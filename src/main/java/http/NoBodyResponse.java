package http;

import java.util.Map;

public class NoBodyResponse extends HttpResponse {

    public NoBodyResponse(HttpRequest request) {
        super(request);
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.OK;
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
