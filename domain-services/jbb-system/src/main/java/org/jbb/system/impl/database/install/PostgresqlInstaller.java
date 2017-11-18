/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.install;

import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.install.database.PostgresqlInstallationData;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.postgres.PostgresqlSettings;
import org.springframework.stereotype.Component;

@Component
public class PostgresqlInstaller implements DbProviderInstaller {

    @Override
    public boolean isApplicable(DatabaseProvider databaseProvider) {
        return databaseProvider == DatabaseProvider.POSTGRESQL;
    }

    @Override
    public void apply(DatabaseInstallationData dbInstallData, DatabaseSettings databaseSettings) {
        PostgresqlSettings postgresqlSettings = databaseSettings.getPostgresqlSettings();
        PostgresqlInstallationData postgresData = dbInstallData.getPostgresqlInstallationData();
        postgresqlSettings.setHostName(postgresData.getHostName());
        postgresqlSettings.setPort(postgresData.getPort());
        postgresqlSettings.setDatabaseName(postgresData.getDatabaseName());
        postgresqlSettings.setUsername(postgresData.getUsername());
        postgresqlSettings.setPassword(postgresData.getPassword());
    }
}
