package io;

import http.HttpResponse;

import java.io.IOException;
import java.io.OutputStream;

public class HttpResponseWriter implements Writer<HttpResponse> {

    private final OutputStream out;

    public HttpResponseWriter(OutputStream out) {
        this.out = out;
    }

    @Override
    public void write(HttpResponse response) throws IOException {
        out.write(response.status());
        out.write(response.headers());
        out.write(response.body());
        out.flush();
    }

}
