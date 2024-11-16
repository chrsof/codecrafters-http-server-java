package http;

import config.Environment;
import util.Strings;

public final class HttpResponse {

    public static String getResponse(String path) {
        StringBuilder builder = new StringBuilder();
        return switch (path) {
            case "/" -> builder.append(Environment.HTTP_VERSION).append(Strings.SPACE)
                    .append(HttpStatus.OK)
                    .append(Strings.CRLF.repeat(2))
                    .toString();
            case String p when p.startsWith("/echo/") -> {
                String word = Strings.after(path, "/echo/");
                yield builder.append(Environment.HTTP_VERSION).append(Strings.SPACE)
                        .append(HttpStatus.OK)
                        .append(Strings.CRLF)
                        .append("Content-Type: text/plain")
                        .append(Strings.CRLF)
                        .append("Content-Length:").append(Strings.SPACE).append(word.length())
                        .append(Strings.CRLF.repeat(2))
                        .append(word)
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
