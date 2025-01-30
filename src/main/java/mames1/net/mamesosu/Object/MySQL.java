package mames1.net.mamesosu.Object;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    final String HOST;
    final String USER;
    final String PASSWORD;
    final String DATABASE;

    public MySQL() {
        Dotenv dotenv = Dotenv.configure()
                .load();
        HOST = dotenv.get("DB_HOST");
        USER = dotenv.get("DB_USER");
        PASSWORD = dotenv.get("DB_PASS");
        DATABASE = dotenv.get("DATABASE");
    }

    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:mysql://" + HOST + "/" + DATABASE + "?useSSL=false",
                USER,
                PASSWORD
        );
    }
}
