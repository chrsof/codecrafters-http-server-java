package http;

import config.Environment;
import util.File;
import util.Strings;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

public class ReadFileResponse extends HttpResponse {

    private final String contents;

    public ReadFileResponse(HttpRequest request) {
        super(request);
        String directory = Objects.requireNonNullElse(Environment.getInstance().getDirectory(), ".");
        String filename = request.getPath().startsWith("/files/") ? Strings.after(request.getPath(), "/files/") : "";

        Path filepath = Path.of(directory, filename);
        this.contents = File.readString(filepath).orElse(null);
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return Optional.ofNullable(contents)
                .map(_ -> HttpStatus.OK)
                .orElse(HttpStatus.NOT_FOUND);
    }

    @Override
    protected Map<HttpHeader, String> getResponseHeaders() {
        Map<HttpHeader, String> headers = new HashMap<>(super.getResponseHeaders());
        if (Objects.nonNull(contents)) {
            headers.put(HttpHeader.CONTENT_TYPE, "application/octet-stream");
            headers.put(HttpHeader.CONTENT_LENGTH, String.valueOf(contents.length()));
        }
        return headers;
    }

    @Override
    public byte[] body() {
        return Optional.ofNullable(contents)
                .map(string -> string.getBytes(StandardCharsets.UTF_8))
                .orElseGet(() -> new byte[0]);
    }

}
