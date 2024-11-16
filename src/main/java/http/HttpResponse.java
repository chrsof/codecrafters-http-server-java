package http;

import config.Environment;
import util.Strings;

public final class HttpResponse {

    public static String getResponse(HttpRequest request) {
        StringBuilder builder = new StringBuilder();
        return switch (request.getPath()) {
            case "/" -> builder.append(Environment.HTTP_VERSION).append(Strings.SPACE)
                    .append(HttpStatus.OK)
                    .append(Strings.CRLF.repeat(2))
                    .toString();
            case String p when p.startsWith("/echo/") || ("/user-agent".equals(p) && request.getHeaders().containsKey(HttpHeader.USER_AGENT)) -> {
                String value = p.startsWith("/echo/") ?
                        Strings.after(p, "/echo/") :
                        request.getHeaders().get(HttpHeader.USER_AGENT);
                yield builder.append(Environment.HTTP_VERSION).append(Strings.SPACE)
                        .append(HttpStatus.OK)
                        .append(Strings.CRLF)
                        .append(HttpHeader.CONTENT_TYPE).append(Strings.COLON).append(Strings.SPACE).append("text/plain")
                        .append(Strings.CRLF)
                        .append(HttpHeader.CONTENT_LENGTH).append(Strings.COLON).append(Strings.SPACE).append(value.length())
                        .append(Strings.CRLF.repeat(2))
                        .append(value)
                        .toString();
            }
            case null, default -> builder.append(Environment.HTTP_VERSION)
                    .append(Strings.SPACE)
                    .append(HttpStatus.NOT_FOUND)
                    .append(Strings.CRLF.repeat(2))
                    .toString();
        };
    }

}
