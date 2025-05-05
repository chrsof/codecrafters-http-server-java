package http;

import io.HttpRequestReader;
import io.HttpResponseWriter;
import io.Reader;
import io.Writer;

import java.io.*;
import java.net.Socket;

public class ConnectionHandler implements Runnable {

    private final Socket connection;

    public ConnectionHandler(Socket connection) {
        this.connection = connection;
    }

    @Override
    public void run() {
        System.out.println("accepted new connection");
        try (connection;
             BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
             BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream())) {
            connection.setSoTimeout(1_000);
            bis.mark(1);
            while (!connection.isClosed() && bis.read() != -1) {
                Reader<HttpRequest> reader = new HttpRequestReader(bis);
                HttpRequest request = reader.read();
                bis.mark(bis.available());

                HttpResponse response = HttpResponseFactory.getResponse(request);
                Writer<HttpResponse> writer = new HttpResponseWriter(bos);
                writer.write(response);

                if (request.isNonPersistenceRequested()) {
                    break;
                }
            }
        } catch (IOException ioe) {
            System.err.printf("IOException: %s%n", ioe.getMessage());
        }
    }

}
