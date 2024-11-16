package http;

import java.util.Arrays;

public enum HttpHeader {
    HOST("Host"),
    USER_AGENT("User-Agent"),
    ACCEPT("Accept"),
    CONTENT_TYPE("Content-Type"),
    CONTENT_LENGTH("Content-Length");

    private final String header;

    HttpHeader(String header) {
        this.header = header;
    }

    public static HttpHeader parseHeader(String value) {
        return Arrays.stream(HttpHeader.values())
                .filter(h -> h.header.equals(value))
                .findFirst()
                .orElse(null);
    }

    @Override
    public String toString() {
        return header;
    }

}
