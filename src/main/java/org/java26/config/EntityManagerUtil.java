package org.java26.config;

import io.github.cdimascio.dotenv.Dotenv;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

import java.util.HashMap;
import java.util.Map;

public class EntityManagerUtil {

    private static final EntityManagerFactory emf;

    static {
        Dotenv dotenv = Dotenv.load();

        Map<String, Object> properties = new HashMap<>();

        String url = "jdbc:postgresql://localhost:5432/"
                + dotenv.get("POSTGRES_DB");

        properties.put("jakarta.persistence.jdbc.url", url);
        properties.put("jakarta.persistence.jdbc.user",
                dotenv.get("POSTGRES_USER"));
        properties.put("jakarta.persistence.jdbc.password",
                dotenv.get("POSTGRES_PASSWORD"));
        properties.put("jakarta.persistence.jdbc.driver",
                "org.postgresql.Driver");

        emf = Persistence.createEntityManagerFactory(
                "pokemonPU",
                properties
        );
    }

    public static EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public static void close() {
        if (emf.isOpen()) {
            emf.close();
        }
    }
}