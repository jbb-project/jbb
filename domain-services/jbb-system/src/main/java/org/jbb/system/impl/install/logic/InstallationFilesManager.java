/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.install.logic;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.configuration2.FileBasedConfiguration;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.EnumUtils;
import org.jbb.install.InstallationData;
import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.install.database.DatabaseType;
import org.jbb.install.database.H2EmbeddedInstallationData;
import org.jbb.install.database.H2ManagedServerInstallationData;
import org.jbb.install.database.H2RemoteServerInstallationData;
import org.jbb.install.database.PostgresqlInstallationData;
import org.jbb.lib.commons.JbbMetaData;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class InstallationFilesManager {

    private static final String AUTO_INSTALL_FILE_NAME = "jbb-autoinstall.properties";
    private static final String INSTALL_FILE_NAME = "installation.data";

    private static final String ADMIN_USERNAME = "admin.username";
    private static final String ADMIN_DISPLAYED_NAME = "admin.displayedName";
    private static final String ADMIN_EMAIL = "admin.email";
    private static final String ADMIN_PSWD = "admin.password";
    private static final String BOARD_NAME = "board.name";

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



    private final JbbMetaData jbbMetaData;

    public Optional<InstallationData> readAutoInstallFile() {
        File autoInstallFile = getAutoInstallFile();
        if (autoInstallFile.exists()) {
            return Optional.of(buildInstallationData(autoInstallFile));
        } else {
            return Optional.empty();
        }

    }

    private InstallationData buildInstallationData(File autoInstallFile) {
        Parameters params = new Parameters();
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
            new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(params.fileBased()
                    .setFile(autoInstallFile));
        try {
            FileBasedConfiguration configuration = builder.getConfiguration();
            return InstallationData.builder()
                .adminUsername(configuration.getString(ADMIN_USERNAME, null))
                .adminDisplayedName(configuration.getString(ADMIN_DISPLAYED_NAME, null))
                .adminEmail(configuration.getString(ADMIN_EMAIL, null))
                .adminPassword(configuration.getString(ADMIN_PSWD, null))
                .boardName(configuration.getString(BOARD_NAME, null))
                .databaseInstallationData(buildDatabaseInstallationData(configuration))
                .build();
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    private DatabaseInstallationData buildDatabaseInstallationData(
        FileBasedConfiguration configuration) {
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

    public void createInstallationFile(InstallationData installationData) {
        Parameters params = new Parameters();
        File installFile = getInstallFile();
        try {
            FileUtils.touch(installFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
        FileBasedConfigurationBuilder<FileBasedConfiguration> builder =
            new FileBasedConfigurationBuilder<FileBasedConfiguration>(PropertiesConfiguration.class)
                .configure(params.fileBased()
                    .setFile(installFile));
        builder.setAutoSave(true);

        try {
            FileBasedConfiguration configuration = builder.getConfiguration();
            configuration.addProperty("installationId", UUID.randomUUID().toString());
            configuration.addProperty("installationVersion", jbbMetaData.jbbVersion());
            configuration.addProperty("installationDate", LocalDateTime.now().toString());
            configuration.addProperty("boardFounderUsername", installationData.getAdminUsername());
            builder.save();
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    public boolean installationFileExists() {
        return getInstallFile().exists();
    }

    public boolean removeAutoInstallFile() {
        try {
            File autoInstallFile = getAutoInstallFile();
            if (autoInstallFile.exists()) {
                FileUtils.forceDelete(autoInstallFile);
                return true;
            }
            return false;
        } catch (IOException e) {
            log.warn("Cannot remove auto install file", e);
            return false;
        }
    }

    private File getAutoInstallFile() {
        return new File(jbbMetaData.jbbHomePath() + File.separator + AUTO_INSTALL_FILE_NAME);
    }

    private File getInstallFile() {
        return new File(jbbMetaData.jbbHomePath() + File.separator + INSTALL_FILE_NAME);
    }

}
