package mames1.net.mamesosu.API;

import com.sun.net.httpserver.HttpServer;
import io.github.cdimascio.dotenv.Dotenv;
import lombok.Getter;
import mames1.net.mamesosu.API.Handler.RequestChecker;

import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Getter
public class Server {

    List<Map<Integer, List<String>>> userList; //{"id": {"unix", "key"}}
    String secretKey;
    int port;

    public Server() {
        Dotenv dotenv = Dotenv.configure().load();
        secretKey = dotenv.get("SECRET_KEY");
        port = Integer.parseInt(dotenv.get("port"));
        userList = new ArrayList<>();
    }

    public void startServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/check_request", new RequestChecker());
            server.setExecutor(null); // creates a default executor

            System.out.println("Web server started on port " + port);

            server.start();

        } catch (Exception e) {
            System.out.println("Error starting web server: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
