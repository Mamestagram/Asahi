package mames1.net.mamesosu.API.Handler;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import mames1.net.mamesosu.API.Server;
import mames1.net.mamesosu.Main;
import mames1.net.mamesosu.Utils.Password;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Map;

public class RequestChecker implements HttpHandler {

    /*
    * id: int
    * unix: long
    * secret_key: str
    * */

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if ("POST".equalsIgnoreCase(exchange.getRequestMethod())) {

            InputStreamReader isr = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            ObjectMapper mapper = new ObjectMapper();
            BufferedReader reader = new BufferedReader(isr);
            StringBuilder body = new StringBuilder();
            Server server = Main.server;
            List<Map<Integer, List<String>>> userList = server.getUserList();

            String key = server.getSecretKey();

            JsonNode node;
            String line;

            while ((line = reader.readLine()) != null) {
                body.append(line);
            }

            node = mapper.readTree(body.toString());

            // bancho.py側でusernameをidに変換
            int id = node.get("id").asInt();
            long unix = node.get("unix").asLong();
            String jsonKey = node.get("key").asText();

            if (!jsonKey.equals(key)) {
                // keyが異なる
                exchange.sendResponseHeaders(401, -1);
                return;
            }

            for(Map<Integer, List<String>> u : userList) {
                if(u.containsKey(id)) {
                    List<String> values = u.get(id);
                    // リクエスト送りすぎエラー
                    if(unix - Long.parseLong(values.get(0)) < 10000) { // 10s
                        exchange.sendResponseHeaders(402, -1);
                        return;
                    }

                    u.clear();
                }
            }

            // 総チェック完了

            try {
                userList.add(Map.of(id, List.of(String.valueOf(unix), Password.getHashPassword(String.valueOf(unix)))));
            } catch (NoSuchAlgorithmException ex) {
                ex.fillInStackTrace();
            }

            exchange.sendResponseHeaders(200, -1);
        }
    }
}

