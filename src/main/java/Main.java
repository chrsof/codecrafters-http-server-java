import config.Environment;
import http.HttpServer;

public class Main {

    public static void main(String[] args) {
        Environment.getInstance().parseArgs(args);
        HttpServer server = new HttpServer();
        server.start();
    }

}
