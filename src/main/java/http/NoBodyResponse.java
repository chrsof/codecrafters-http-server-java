package http;

public class NoBodyResponse extends HttpResponse {

    public NoBodyResponse(HttpRequest request) {
        super(request);
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.OK;
    }

    @Override
    public byte[] body() {
        return new byte[0];
    }

}
