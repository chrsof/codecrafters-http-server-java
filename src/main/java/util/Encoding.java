package util;

import java.util.Arrays;

public enum Encoding {
    GZIP("gzip");

    private final String encoding;

    Encoding(String encoding) {
        this.encoding = encoding;
    }

    public String getEncoding() {
        return encoding;
    }

    public static Encoding parseEncoding(String encoding) {
        return Arrays.stream(Encoding.values())
                .filter(e -> e.encoding.equals(encoding))
                .findFirst()
                .orElse(null);
    }

}
