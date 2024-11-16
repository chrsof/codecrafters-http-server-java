package io;

import java.io.IOException;

@FunctionalInterface
public interface Writer<T> {
    void write(T response) throws IOException;
}
