package http;

public class NotFoundResponse extends HttpResponse {

    public NotFoundResponse(HttpRequest request) {
        super(request);
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.NOT_FOUND;
    }

    @Override
    public byte[] body() {
        return new byte[0];
    }

}
