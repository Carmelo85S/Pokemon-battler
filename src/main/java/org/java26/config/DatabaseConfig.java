package org.java26.config;

import io.github.cdimascio.dotenv.Dotenv;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
    public static final Dotenv dotenv = Dotenv.load();

    public static final String URL = dotenv.get("POSTGRES_URL_PREFIX") + dotenv.get("POSTGRES_DB");

    public static final String USER= dotenv.get("POSTGRES_USER");
    public static final String PASSWORD = dotenv.get("POSTGRES_PASSWORD");

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
