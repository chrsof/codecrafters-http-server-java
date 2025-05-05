package http;

import config.Environment;
import util.File;
import util.Strings;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class WriteFileResponse extends HttpResponse {

    private final Path filepath;

    public WriteFileResponse(HttpRequest request) {
        super(request);
        String directory = Objects.requireNonNullElse(Environment.getInstance().getDirectory(), ".");
        String filename = request.getPath().startsWith("/files/") ? Strings.after(request.getPath(), "/files/") : "";

        this.filepath = Path.of(directory, filename);
        File.writeString(filepath, request.getBody());
    }

    @Override
    protected HttpStatus getResponseStatus() {
        return HttpStatus.CREATED;
    }

    @Override
    protected Map<HttpHeader, String> getResponseHeaders() {
        Map<HttpHeader, String> headers = new HashMap<>(super.getResponseHeaders());
        headers.put(HttpHeader.CONTENT_TYPE, "application/octet-stream");
        headers.put(HttpHeader.CONTENT_LENGTH, String.valueOf(File.size(filepath)));
        return headers;
    }

    @Override
    public byte[] body() {
        return File.readString(filepath)
                .map(string -> string.getBytes(StandardCharsets.UTF_8))
                .orElseGet(() -> new byte[0]);
    }

}
