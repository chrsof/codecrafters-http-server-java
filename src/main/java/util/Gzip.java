package util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.zip.GZIPOutputStream;

public final class Gzip {

    private Gzip() {
    }

    public static byte[] compress(String content) {
        if (Objects.isNull(content) || content.isBlank()) {
            return new byte[0];
        }

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream(content.length());
             GZIPOutputStream gos = new GZIPOutputStream(baos)) {
            gos.write(content.getBytes(StandardCharsets.UTF_8));
            gos.finish();
            gos.flush();
            return baos.toByteArray();
        } catch (IOException ioe) {
            return new byte[0];
        }
    }

}
