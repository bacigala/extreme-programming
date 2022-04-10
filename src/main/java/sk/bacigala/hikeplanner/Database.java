package sk.bacigala.hikeplanner;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {

    private static String getEnvironmentVariable(String name) {
        if (name == null || name.isEmpty())
            return null;

        final Dotenv dotenv = Dotenv.configure().ignoreIfMalformed().load();
        return dotenv.get(name);
    }

    public static Connection connect() throws SQLException {
        String url = "jdbc:postgresql://" +
                getEnvironmentVariable("DB_HOST") +
                ":" +
                getEnvironmentVariable("DB_PORT") +
                "/" +
                getEnvironmentVariable("DB_NAME");

        String username = getEnvironmentVariable("DB_USER");
        String password = getEnvironmentVariable("DB_PASSWORD");

        return DriverManager.getConnection(url, username, password);
    }
}
