package com.examplo.cdc.cdcpostgres.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class DebeziumConnectorConfig {

    private final String postgresUrl;
    private final String postgresUsername;
    private final String postgresPassword;

    private static final String POSTGRES = "postgres";

    DebeziumConnectorConfig(@Value("${spring.datasource.url}") String postgresUrl,
                            @Value("${spring.datasource.username}") String postgresUsername,
                            @Value("${spring.datasource.password}") String postgresPassword) {

        this.postgresUrl = postgresUrl;
        this.postgresUsername = postgresUsername;
        this.postgresPassword = postgresPassword;
    }

    @Bean
    public io.debezium.config.Configuration postgresDbConnector() {
        return io.debezium.config.Configuration.create()
            .with("name", "postgres_connector")
            .with("connector.class", "io.debezium.connector.postgresql.PostgresConnector")
            .with("offset.storage", "io.debezium.storage.jdbc.offset.JdbcOffsetBackingStore")
            .with("offset.storage.jdbc.url", postgresUrl)
            .with("offset.storage.jdbc.user", postgresUsername)
            .with("offset.storage.jdbc.password", postgresPassword)
            .with("database.hostname", POSTGRES)
            .with("database.port", "5432")
            .with("database.user", postgresUsername)
            .with("database.password", postgresPassword)
            .with("database.dbname", POSTGRES)
            .with("database.server.id", "10181")
            .with("database.server.name", "cdc-postgres-db-server")
            .with("database.history", "io.debezium.relational.history.MemoryDatabaseHistory")
            .with("table.include.list", "public.teste_outbox")
            .with("publication.autocreate.mode", "filtered")
            .with("plugin.name", "pgoutput")
            .with("slot.name", "dbz_postgresdb_listener")
            .with("errors.log.include.messages", "true")
            .with("topic.prefix", POSTGRES)
            .build();
    }

}
