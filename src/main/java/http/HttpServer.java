package http;

import config.Environment;
import util.Regex;
import util.Strings;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class HttpServer {

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(Environment.PORT)) {
            serverSocket.setReuseAddress(true);
            try (Socket connection = serverSocket.accept()) {
                System.out.println("accepted new connection");
                try (Scanner in = new Scanner(connection.getInputStream());
                     PrintWriter out = new PrintWriter(connection.getOutputStream(), true, StandardCharsets.UTF_8)) {

                    Pattern pattern = Regex.httpRequestTargetPattern.get();

                    String target = null;
                    while (in.hasNextLine()) {
                        String line = in.nextLine();
                        System.out.println(line);
                        Matcher matcher = pattern.matcher(line);
                        if (matcher.find()) {
                            target = matcher.group(1);
                            break;
                        }
                    }

                    if ("/".equals(target)) {
                        out.write("HTTP/1.1 200 OK%s".formatted(Strings.CRLF.repeat(2)));
                    } else {
                        out.write("HTTP/1.1 404 Not Found%s".formatted(Strings.CRLF.repeat(2)));
                    }
                    out.flush();
                }
            }
        } catch (IOException e) {
            System.err.printf("IOException: %s%n", e.getMessage());
        }
    }

}
