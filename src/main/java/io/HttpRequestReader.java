package io;

import http.HttpHeader;
import http.HttpMethod;
import http.HttpRequest;
import util.Regex;
import util.Strings;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;

public class HttpRequestReader implements Reader<HttpRequest> {

    private final InputStream in;

    public HttpRequestReader(InputStream in) {
        this.in = in;
    }

    @Override
    public HttpRequest read() throws IOException {
        String input = parseInputStream();
        Objects.requireNonNull(input);

        HttpRequest.Builder request = HttpRequest.builder();

        List<String> inputLines = List.of(input.split(Strings.CRLF));

        for (String line : inputLines) {
            if (line.isBlank()) {
                continue;
            }

            Matcher matcher = Regex.httpRequestPattern.get().matcher(line);
            if (matcher.find()) {
                String method = matcher.group(1);
                request = request.forMethod(HttpMethod.valueOf(method));

                String path = matcher.group(2);
                request = request.withPath(path);

                continue;
            }

            matcher = Regex.httpHeaderPattern.get().matcher(line);
            if (matcher.find()) {
                Optional<HttpHeader> header = HttpHeader.parseHeader(matcher.group(1));
                String value = matcher.group(2);
                if (header.isPresent() && Objects.nonNull(value)) {
                    request = request.addHeader(header.get(), value);
                }

                continue;
            }

            request = request.withBody(line);
        }

        return request.build();
    }

    private String parseInputStream() throws IOException {
        StringBuilder request = new StringBuilder();
        while (in.available() != 0) {
            request.append((char) in.read());
        }
        return request.toString();
    }

}
