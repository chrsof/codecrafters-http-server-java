package io;

import java.io.IOException;

@FunctionalInterface
public interface Reader<T> {
    T read() throws IOException;
}
