package sk.bacigala.hikeplanner;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static String getEnviromentVariable(String name) {
        if (name == null || name.isEmpty())
            return null;

        final Dotenv dotenv = Dotenv.configure().ignoreIfMalformed().load();
        return dotenv.get(name);
    }

    public static Connection connect() throws SQLException {
        String url = "jdbc:postgresql://" +
                getEnviromentVariable("DB_HOST") +
                ":" +
                getEnviromentVariable("DB_PORT") +
                "/" +
                getEnviromentVariable("DB_NAME");

        String username = getEnviromentVariable("DB_USER");
        String password = getEnviromentVariable("DB_PASSWORD");

        return DriverManager.getConnection(url, username, password);
    }
}
