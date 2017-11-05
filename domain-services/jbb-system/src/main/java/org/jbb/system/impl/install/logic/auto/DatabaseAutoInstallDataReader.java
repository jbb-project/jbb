/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install.logic.auto;

import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.lang3.EnumUtils;
import org.jbb.install.InstallationData;
import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.install.database.DatabaseType;
import org.jbb.install.database.H2EmbeddedInstallationData;
import org.jbb.install.database.H2ManagedServerInstallationData;
import org.jbb.install.database.H2RemoteServerInstallationData;
import org.jbb.install.database.PostgresqlInstallationData;
import org.springframework.stereotype.Component;

@Component
public class DatabaseAutoInstallDataReader implements AutoInstallationDataReader {

    private static final String DB_TYPE = "database.type";

    private static final String DB_H2_EMBEDDED_USERNAME = "database.h2.embedded.username";
    private static final String DB_H2_EMBEDDED_FILENAME = "database.h2.embedded.filename";
    private static final String DB_H2_EMBEDDED_PSWD = "database.h2.embedded.password";

    private static final String DB_H2_MANAGED_USERNAME = "database.h2.managed.username";
    private static final String DB_H2_MANAGED_FILENAME = "database.h2.managed.filename";
    private static final String DB_H2_MANAGED_PORT = "database.h2.managed.port";
    private static final String DB_H2_MANAGED_PSWD = "database.h2.managed.password";

    private static final String DB_H2_REMOTE_USERNAME = "database.h2.remote.username";
    private static final String DB_H2_REMOTE_URL = "database.h2.remote.filename";
    private static final String DB_H2_REMOTE_PSWD = "database.h2.remote.password";

    private static final String DB_POSTGRES_HOSTNAME = "database.postgres.hostname";
    private static final String DB_POSTGRES_PORT = "database.postgres.port";
    private static final String DB_POSTGRES_DATABASE_NAME = "database.postgres.dbname";
    private static final String DB_POSTGRES_USERNAME = "database.postgres.username";
    private static final String DB_POSTGRES_PSWD = "database.postgres.password";

    @Override
    public InstallationData updateInstallationData(InstallationData data,
        FileBasedConfiguration configuration) {
        data.setDatabaseInstallationData(buildDatabaseData(configuration));
        return data;
    }

    private DatabaseInstallationData buildDatabaseData(FileBasedConfiguration configuration) {
        DatabaseType databaseType = EnumUtils
            .getEnum(DatabaseType.class, configuration.getString(DB_TYPE, null));
        if (databaseType == null) {
            databaseType = DatabaseType.H2_EMBEDDED;
        }
        return DatabaseInstallationData.builder()
            .databaseType(databaseType)
            .h2EmbeddedInstallationData(
                H2EmbeddedInstallationData.builder()
                    .username(configuration.getString(DB_H2_EMBEDDED_USERNAME, null))
                    .databaseFileName(configuration.getString(DB_H2_EMBEDDED_FILENAME, null))
                    .usernamePassword(configuration.getString(DB_H2_EMBEDDED_PSWD, null))
                    .build()
            )
            .h2ManagedServerInstallationData(
                H2ManagedServerInstallationData.builder()
                    .username(configuration.getString(DB_H2_MANAGED_USERNAME, null))
                    .databaseFileName(configuration.getString(DB_H2_MANAGED_FILENAME, null))
                    .port(configuration.getInt(DB_H2_MANAGED_PORT, 0))
                    .usernamePassword(configuration.getString(DB_H2_MANAGED_PSWD, null))
                    .build()
            )
            .h2RemoteServerInstallationData(
                H2RemoteServerInstallationData.builder()
                    .username(configuration.getString(DB_H2_REMOTE_USERNAME, null))
                    .url(configuration.getString(DB_H2_REMOTE_URL, null))
                    .usernamePassword(configuration.getString(DB_H2_REMOTE_PSWD, null))
                    .build()
            )
            .postgresqlInstallationData(
                PostgresqlInstallationData.builder()
                    .hostName(configuration.getString(DB_POSTGRES_HOSTNAME, null))
                    .port(configuration.getInt(DB_POSTGRES_PORT, 0))
                    .databaseName(configuration.getString(DB_POSTGRES_DATABASE_NAME, null))
                    .username(configuration.getString(DB_POSTGRES_USERNAME, null))
                    .password(configuration.getString(DB_POSTGRES_PSWD, null))
                    .build()
            )
            .build();
    }
}
