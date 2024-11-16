package http;

import config.Environment;
import util.Strings;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Objects;

public class WriteFileResponse extends HttpResponse {

    private final Path filepath;

    public WriteFileResponse(HttpRequest request) {
        super(request);

        String directory = Objects.requireNonNullElse(Environment.getInstance().getDirectory(), ".");
        String filename = request.getPath().startsWith("/files/") ? Strings.after(request.getPath(), "/files/") : "";

        this.filepath = Path.of(directory, filename);
        try {
            Files.writeString(filepath, request.getBody());
        } catch (IOException ioe) {
            // noop
        }
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.CREATED;
    }

    @Override
    protected Map<HttpHeader, String> getResponseHeaders() {
        long size = 0L;
        try {
            size = Files.size(filepath);
        } catch (IOException ioe) {
            // noop
        }

        return Map.of(
                HttpHeader.CONTENT_TYPE, "application/octet-stream",
                HttpHeader.CONTENT_LENGTH, String.valueOf(size)
        );
    }

    @Override
    public byte[] body() {
        try {
            return Files.readString(filepath).getBytes(StandardCharsets.UTF_8);
        } catch (IOException ioe) {
            return new byte[0];
        }
    }

}
