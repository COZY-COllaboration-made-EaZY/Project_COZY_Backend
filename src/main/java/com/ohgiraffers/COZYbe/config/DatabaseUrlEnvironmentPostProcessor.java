package com.ohgiraffers.COZYbe.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseUrlEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

    private static final String SOURCE_NAME = "render-database-url";

    @Override
    public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
        String existingUrl = environment.getProperty("spring.datasource.url");
        if (existingUrl != null && !existingUrl.isBlank()) {
            return;
        }

        String databaseUrl = environment.getProperty("DATABASE_URL");
        if (databaseUrl == null || databaseUrl.isBlank()) {
            return;
        }

        DatabaseUrlParts parts = parseDatabaseUrl(databaseUrl);
        if (parts == null) {
            return;
        }

        Map<String, Object> props = new HashMap<>();
        props.put("spring.datasource.url", parts.jdbcUrl);

        String username = environment.getProperty("spring.datasource.username");
        if (username == null || username.isBlank()) {
            props.put("spring.datasource.username", parts.username);
        }

        String password = environment.getProperty("spring.datasource.password");
        if (password == null || password.isBlank()) {
            props.put("spring.datasource.password", parts.password);
        }

        environment.getPropertySources().addFirst(new MapPropertySource(SOURCE_NAME, props));
    }

    @Override
    public int getOrder() {
        return Ordered.HIGHEST_PRECEDENCE;
    }

    private DatabaseUrlParts parseDatabaseUrl(String databaseUrl) {
        try {
            URI uri = new URI(databaseUrl);
            String scheme = uri.getScheme();
            if (scheme == null || (!scheme.equals("postgres") && !scheme.equals("postgresql"))) {
                return null;
            }

            String userInfo = uri.getUserInfo();
            if (userInfo == null || !userInfo.contains(":")) {
                return null;
            }

            String[] userParts = userInfo.split(":", 2);
            String username = userParts[0];
            String password = userParts[1];

            String host = uri.getHost();
            int port = uri.getPort();
            String path = uri.getPath();
            if (host == null || host.isBlank() || path == null || path.isBlank()) {
                return null;
            }

            String database = path.startsWith("/") ? path.substring(1) : path;
            String jdbcUrl = "jdbc:postgresql://" + host + (port > 0 ? ":" + port : "") + "/" + database;
            if (uri.getQuery() != null && !uri.getQuery().isBlank()) {
                jdbcUrl = jdbcUrl + "?" + uri.getQuery();
            }

            return new DatabaseUrlParts(jdbcUrl, username, password);
        } catch (URISyntaxException e) {
            return null;
        }
    }

    private static final class DatabaseUrlParts {
        private final String jdbcUrl;
        private final String username;
        private final String password;

        private DatabaseUrlParts(String jdbcUrl, String username, String password) {
            this.jdbcUrl = jdbcUrl;
            this.username = username;
            this.password = password;
        }
    }
}
