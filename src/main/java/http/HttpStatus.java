package http;

public enum HttpStatus {
    OK("OK", 200),
    CREATED("Created", 201),
    NOT_FOUND("Not Found", 404);

    private final String status;
    private final int code;

    HttpStatus(String status, int code) {
        this.status = status;
        this.code = code;
    }

    @Override
    public String toString() {
        return "%d %s".formatted(code, status);
    }

}
