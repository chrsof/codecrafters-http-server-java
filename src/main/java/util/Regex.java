package util;

import http.HttpMethod;

import java.util.function.Supplier;
import java.util.regex.Pattern;

public final class Regex {

    public static final Supplier<Pattern> httpRequestPattern = () -> Pattern.compile("(^%s)\\s(\\/[\\S]*).*".formatted(HttpMethod.getAllMethods()));

    public static final Supplier<Pattern> httpHeaderPattern = () -> Pattern.compile("(\\w+-*\\w+):\\s(.+)(\\r\\n)?");

}
