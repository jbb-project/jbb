/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.logic;

import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.jbb.install.InstallAction;
import org.jbb.install.InstallationData;
import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.install.database.H2EmbeddedInstallationData;
import org.jbb.install.database.H2ManagedServerInstallationData;
import org.jbb.install.database.H2RemoteServerInstallationData;
import org.jbb.install.database.PostgresqlInstallationData;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.DatabaseSettingsService;
import org.jbb.system.api.database.h2.H2EmbeddedSettings;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.jbb.system.api.database.h2.H2RemoteServerSettings;
import org.jbb.system.api.database.postgres.PostgresqlSettings;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
@RequiredArgsConstructor
public class DatabaseInstallationAction implements InstallAction {

    private final DatabaseSettingsService databaseSettingsService;

    @Override
    public void install(InstallationData installationData) {
        DatabaseInstallationData dbInstallData = installationData.getDatabaseInstallationData();
        DatabaseSettings databaseSettings = databaseSettingsService.getDatabaseSettings();
        DatabaseProvider databaseProvider = EnumUtils
            .getEnum(DatabaseProvider.class, dbInstallData.getDatabaseType().toString());
        databaseSettings.setCurrentDatabaseProvider(databaseProvider);

        if (databaseProvider == DatabaseProvider.H2_EMBEDDED) {
            H2EmbeddedSettings h2EmbeddedSettings = databaseSettings.getH2EmbeddedSettings();
            H2EmbeddedInstallationData embeddedData = dbInstallData.getH2EmbeddedInstallationData();
            h2EmbeddedSettings.setUsername(embeddedData.getUsername());
            h2EmbeddedSettings.setDatabaseFileName(embeddedData.getDatabaseFileName());
            h2EmbeddedSettings.setUsernamePassword(embeddedData.getUsernamePassword());
            h2EmbeddedSettings.setFilePassword(embeddedData.getUsernamePassword());
        } else if (databaseProvider == DatabaseProvider.H2_MANAGED_SERVER) {
            H2ManagedServerSettings h2ManagedServerSettings = databaseSettings
                .getH2ManagedServerSettings();
            H2ManagedServerInstallationData managedServerData = dbInstallData
                .getH2ManagedServerInstallationData();
            h2ManagedServerSettings.setUsername(managedServerData.getUsername());
            h2ManagedServerSettings.setDatabaseFileName(managedServerData.getDatabaseFileName());
            h2ManagedServerSettings.setPort(managedServerData.getPort());
            h2ManagedServerSettings.setUsernamePassword(managedServerData.getUsernamePassword());
            h2ManagedServerSettings.setFilePassword(managedServerData.getUsernamePassword());
        } else if (databaseProvider == DatabaseProvider.H2_REMOTE_SERVER) {
            H2RemoteServerSettings h2RemoteSettings = databaseSettings.getH2RemoteServerSettings();
            H2RemoteServerInstallationData remoteData = dbInstallData
                .getH2RemoteServerInstallationData();
            h2RemoteSettings.setUsername(remoteData.getUsername());
            h2RemoteSettings.setUrl(remoteData.getUrl());
            h2RemoteSettings.setUsernamePassword(remoteData.getUsernamePassword());
            h2RemoteSettings.setFilePassword(remoteData.getUsernamePassword());
        } else if (databaseProvider == DatabaseProvider.POSTGRESQL) {
            PostgresqlSettings postgresqlSettings = databaseSettings.getPostgresqlSettings();
            PostgresqlInstallationData postgresData = dbInstallData.getPostgresqlInstallationData();
            postgresqlSettings.setHostName(postgresData.getHostName());
            postgresqlSettings.setPort(postgresData.getPort());
            postgresqlSettings.setDatabaseName(postgresData.getDatabaseName());
            postgresqlSettings.setUsername(postgresData.getUsername());
            postgresqlSettings.setPassword(postgresData.getPassword());
        }

        databaseSettingsService.setDatabaseSettings(databaseSettings);
    }
}
