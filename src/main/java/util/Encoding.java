package util;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public enum Encoding {
    GZIP("gzip");

    private final String encoding;

    Encoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }

    private static Encoding parse(String encoding) {
        return Arrays.stream(Encoding.values())
                .filter(e -> e.encoding.equals(encoding))
                .findFirst()
                .orElse(null);
    }

    public static List<Encoding> parseEncoding(String encoding) {
        if (Objects.isNull(encoding) || encoding.isBlank()) {
            return null;
        }
        return Arrays.stream(encoding.split(","))
                .map(String::strip)
                .map(Encoding::parse)
                .filter(Objects::nonNull)
                .toList();
    }

}
