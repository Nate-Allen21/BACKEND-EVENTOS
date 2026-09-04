package com.senac.eventos.config;

import java.net.URI;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfig {

    @Bean
    DataSource dataSource(DataSourceProperties properties) {
        String url = properties.getUrl();
        if (url != null && url.startsWith("postgresql://")) {
            URI databaseUri = URI.create(url);
            String userInfo = databaseUri.getUserInfo();
            String jdbcUrl = "jdbc:postgresql://" + databaseUri.getHost();
            if (databaseUri.getPort() != -1) {
                jdbcUrl += ":" + databaseUri.getPort();
            }
            if (databaseUri.getRawPath() != null) {
                jdbcUrl += databaseUri.getRawPath();
            }
            if (databaseUri.getRawQuery() != null) {
                jdbcUrl += "?" + databaseUri.getRawQuery();
            }

            properties.setUrl(jdbcUrl);
            if (userInfo != null) {
                String[] credentials = userInfo.split(":", 2);
                properties.setUsername(credentials[0]);
                if (credentials.length == 2) {
                    properties.setPassword(credentials[1]);
                }
            }
        }
        return properties.initializeDataSourceBuilder().build();
    }
}