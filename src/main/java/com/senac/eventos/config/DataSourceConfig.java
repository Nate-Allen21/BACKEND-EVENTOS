package com.senac.eventos.config;

import java.net.URI;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernatePropertiesCustomizer;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(DataSourceProperties.class)
public class DataSourceConfig {

    public static String resolveDriverClassName(String configuredUrl) {
        String url = configuredUrl == null ? "" : configuredUrl.trim();
        if (url.startsWith("jdbc:postgresql://") || url.startsWith("postgresql://")) {
            return "org.postgresql.Driver";
        }
        return "org.h2.Driver";
    }

    public static String resolveHibernateDialect(String configuredUrl) {
        String url = configuredUrl == null ? "" : configuredUrl.trim();
        if (url.startsWith("jdbc:postgresql://") || url.startsWith("postgresql://")) {
            return "org.hibernate.dialect.PostgreSQLDialect";
        }
        return "org.hibernate.dialect.H2Dialect";
    }

    @Bean
    HibernatePropertiesCustomizer hibernatePropertiesCustomizer(
            @Value("${spring.datasource.url:}") String configuredUrl) {
        return properties -> properties.put("hibernate.dialect", resolveHibernateDialect(configuredUrl));
    }

    @Bean
    DataSource dataSource(
            DataSourceProperties properties,
            @Value("${spring.datasource.url}") String configuredUrl,
            @Value("${spring.datasource.username}") String configuredUsername,
            @Value("${spring.datasource.password}") String configuredPassword) {
        String url = configuredUrl;
        properties.setDriverClassName(resolveDriverClassName(configuredUrl));

        if (url != null && (url.startsWith("postgresql://") || url.startsWith("jdbc:postgresql://"))) {
            String uri = url.startsWith("jdbc:") ? url.substring(5) : url;
            URI databaseUri = URI.create(uri);
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
            properties.setUsername(configuredUsername);
            properties.setPassword(configuredPassword);
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
