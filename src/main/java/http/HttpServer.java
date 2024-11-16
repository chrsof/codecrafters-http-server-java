package http;

import config.Environment;
import io.HttpRequestReader;
import io.Reader;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public final class HttpServer {

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(Environment.PORT)) {
            serverSocket.setReuseAddress(true);
            try (Socket connection = serverSocket.accept()) {
                System.out.println("accepted new connection");
                try (InputStream in = connection.getInputStream();
                     PrintWriter out = new PrintWriter(connection.getOutputStream(), true, StandardCharsets.UTF_8)) {
                    Reader<HttpRequest> reader = new HttpRequestReader(in);
                    HttpRequest request = reader.read();

                    String response = HttpResponse.getResponse(request);
                    out.write(response);
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.err.printf("IOException: %s%n", e.getMessage());
        }
    }

}
