package util;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Strings {

    public static final String CRLF = "\r\n";

    public static final String SPACE = " ";

    public static String after(String string, String delimiter) {
        Objects.requireNonNull(string);
        Objects.requireNonNull(delimiter);

        String regex = "%s([^/]+)".formatted(delimiter);
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(string);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return string;
    }

}
