package util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

public final class File {

    public static Optional<String> readString(Path filename) {
        try {
            return Optional.of(Files.readString(filename));
        } catch (IOException ioe) {
            return Optional.empty();
        }
    }

    public static void writeString(Path filename, String content) {
        try {
            Files.writeString(filename, content);
        } catch (IOException ioe) {
            // noop
        }
    }

    public static long size(Path filename) {
        try {
            return Files.size(filename);
        } catch (IOException ioe) {
            return 0L;
        }
    }

}
