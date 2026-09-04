package com.senac.eventos.config;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DataSourceConfigTest {

    @Test
    void deveUsarDriverPostgresQuandoAUrlForPostgres() {
        assertEquals("org.postgresql.Driver", DataSourceConfig.resolveDriverClassName("jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:5432/postgres"));
    }

    @Test
    void deveUsarDialetoPostgresQuandoAUrlForPostgres() {
        assertEquals("org.hibernate.dialect.PostgreSQLDialect", DataSourceConfig.resolveHibernateDialect("jdbc:postgresql://aws-0-sa-east-1.pooler.supabase.com:5432/postgres"));
    }
}
