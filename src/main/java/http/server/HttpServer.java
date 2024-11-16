package http.server;

import http.config.Environment;

import java.io.IOException;
import java.net.ServerSocket;

public final class HttpServer {

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(Environment.PORT)) {
            serverSocket.setReuseAddress(true);
            serverSocket.accept();
            System.out.println("accepted new connection");
        } catch (IOException e) {
            System.err.printf("IOException: %s%n", e.getMessage());
        }
    }

}
