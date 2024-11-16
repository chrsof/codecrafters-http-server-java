package http;

import config.Environment;
import util.ConnectionHandler;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class HttpServer {

    public void start() {
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();
             ServerSocket serverSocket = new ServerSocket(Environment.PORT)) {
            serverSocket.setReuseAddress(true);
            while (true) {
                Socket connection = serverSocket.accept();
                executor.submit(new ConnectionHandler(connection));
            }
        } catch (IOException e) {
            System.err.printf("IOException: %s%n", e.getMessage());
        }
    }

}
