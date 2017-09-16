/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.logic.provider;

import lombok.RequiredArgsConstructor;
import org.jbb.lib.db.DbProperties;
import org.jbb.lib.db.provider.PostgresqlServerProvider;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.postgres.PostgresqlSettings;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostgresqlManager implements DatabaseProviderManager<PostgresqlSettings> {

    public static final String PROVIDER_PROPERTY_VALUE = PostgresqlServerProvider.PROVIDER_VALUE;

    private final DbProperties dbProperties;

    @Override
    public DatabaseProvider getProviderName() {
        return DatabaseProvider.POSTGRESQL;
    }

    @Override
    public PostgresqlSettings getCurrentProviderSettings() {
        return PostgresqlSettings.builder()
            .hostName(dbProperties.postgreSqlDbUrl())
            .port(dbProperties.postgreSqlDbPort())
            .databaseName(dbProperties.postgreSqlDbName())
            .username(dbProperties.postgreSqlUsername())
            .password(dbProperties.postgreSqlPassword())
            .build();
    }

    @Override
    public void setProviderSettings(DatabaseSettings newDatabaseSettings) {

        PostgresqlSettings newProviderSettings = newDatabaseSettings.getPostgresqlSettings();

        dbProperties.setProperty(DbProperties.POSTGRESQL_DB_URL_KEY,
            newProviderSettings.getHostName());
        dbProperties.setProperty(DbProperties.POSTGRESQL_DB_PORT_KEY,
            Integer.toString(newProviderSettings.getPort()));
        dbProperties.setProperty(DbProperties.POSTGRESQL_DB_NAME_KEY,
            newProviderSettings.getDatabaseName());
        dbProperties.setProperty(DbProperties.POSTGRESQL_DB_USERNAME_KEY,
            newProviderSettings.getUsername());
        dbProperties.setProperty(DbProperties.POSTGRESQL_DB_PASS_KEY,
            newProviderSettings.getPassword());
    }
}
