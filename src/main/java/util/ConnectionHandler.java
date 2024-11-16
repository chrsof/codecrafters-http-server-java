package util;

import http.HttpRequest;
import http.HttpResponse;
import io.HttpRequestReader;
import io.HttpResponseWriter;
import io.Reader;
import io.Writer;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket connection;

    public ConnectionHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        System.out.println("accepted new connection");
        try (InputStream in = connection.getInputStream();
             OutputStream out = connection.getOutputStream()) {
            Reader<HttpRequest> reader = new HttpRequestReader(in);
            HttpRequest request = reader.read();
            HttpResponse response = HttpResponseFactory.getResponse(request);
            Writer<HttpResponse> writer = new HttpResponseWriter(out);
            writer.write(response);
        } catch (IOException e) {
            System.err.printf("IOException: %s%n", e.getMessage());
        }
    }

}
