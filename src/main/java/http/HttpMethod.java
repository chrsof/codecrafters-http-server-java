package http;

import java.util.Arrays;
import java.util.stream.Collectors;

public enum HttpMethod {
    GET;

    public static String getAllMethods() {
        return Arrays.stream(HttpMethod.values())
                .map(HttpMethod::name)
                .collect(Collectors.joining("|"));
    }

}
