/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.database.logic;

import org.apache.commons.lang3.StringUtils;
import org.jbb.system.api.database.CommonDatabaseSettings;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2ConnectionType;
import org.jbb.system.api.database.h2.H2EmbeddedSettings;
import org.jbb.system.api.database.h2.H2EncryptionAlgorithm;
import org.jbb.system.api.database.h2.H2InMemorySettings;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.jbb.system.api.database.h2.H2RemoteServerSettings;
import org.jbb.system.api.database.postgres.PostgresqlSettings;
import org.jbb.system.web.database.form.DatabaseSettingsForm;
import org.jbb.system.web.database.form.H2EmbeddedForm;
import org.jbb.system.web.database.form.H2InMemoryForm;
import org.jbb.system.web.database.form.H2ManagedServerForm;
import org.jbb.system.web.database.form.H2RemoteServerForm;
import org.jbb.system.web.database.form.PostgresqlForm;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FormDatabaseTranslator {

    public static final String ENCRYPTION_DISABLED_STRING = "NONE";

    public DatabaseSettingsForm fillDatabaseSettingsForm(DatabaseSettings databaseSettings,
                                                         DatabaseSettingsForm form) {

        CommonDatabaseSettings commonDatabaseSettings = databaseSettings.getCommonSettings();

        form.setMinimumIdleConnections(commonDatabaseSettings.getMinimumIdleConnections());
        form.setMaximumPoolSize(commonDatabaseSettings.getMaximumPoolSize());
        form.setConnectionTimeoutMilliseconds(
                commonDatabaseSettings.getConnectionTimeoutMilliseconds());
        form.setConnectionMaxLifetimeMilliseconds(
                commonDatabaseSettings.getConnectionMaxLifetimeMilliseconds());
        form.setIdleTimeoutMilliseconds(commonDatabaseSettings.getIdleTimeoutMilliseconds());
        form.setValidationTimeoutMilliseconds(
                commonDatabaseSettings.getValidationTimeoutMilliseconds());
        form.setLeakDetectionThresholdMilliseconds(
                commonDatabaseSettings.getLeakDetectionThresholdMilliseconds());
        form.setFailAtStartingImmediately(commonDatabaseSettings.isFailAtStartingImmediately());
        form.setDropDatabaseAtStart(commonDatabaseSettings.isDropDatabaseAtStart());
        form.setAuditEnabled(commonDatabaseSettings.isAuditEnabled());

        H2InMemorySettings h2InMemorySettings = databaseSettings.getH2InMemorySettings();
        H2InMemoryForm h2InMemoryForm = form.getH2inMemorySettings();
        h2InMemoryForm.setDatabaseName(h2InMemorySettings.getDatabaseName());

        H2ManagedServerSettings h2ManagedServerSettings = databaseSettings
                .getH2ManagedServerSettings();
        H2ManagedServerForm h2managedServerForm = form.getH2managedServerSettings();
        h2managedServerForm.setDatabaseFileName(h2ManagedServerSettings.getDatabaseFileName());
        h2managedServerForm.setPort(h2ManagedServerSettings.getPort());
        h2managedServerForm.setUsername(h2ManagedServerSettings.getUsername());
        h2managedServerForm.setUsernamePassword(StringUtils.EMPTY);
        h2managedServerForm.setFilePassword(StringUtils.EMPTY);
        h2managedServerForm
                .setConnectionType(h2ManagedServerSettings.getConnectionType().toString());
        h2managedServerForm.setEncryptionAlgorithm(
                h2ManagedServerSettings.getEncryptionAlgorithm().map(Enum::toString)
                        .orElse(ENCRYPTION_DISABLED_STRING));

        H2EmbeddedSettings h2EmbeddedSettings = databaseSettings.getH2EmbeddedSettings();
        H2EmbeddedForm h2EmbeddedForm = form.getH2embeddedSettings();
        h2EmbeddedForm.setDatabaseFileName(h2EmbeddedSettings.getDatabaseFileName());
        h2EmbeddedForm.setUsername(h2EmbeddedSettings.getUsername());
        h2EmbeddedForm.setUsernamePassword(StringUtils.EMPTY);
        h2EmbeddedForm.setFilePassword(StringUtils.EMPTY);
        h2EmbeddedForm.setEncryptionAlgorithm(
                h2EmbeddedSettings.getEncryptionAlgorithm().map(Enum::toString)
                        .orElse(ENCRYPTION_DISABLED_STRING));

        H2RemoteServerSettings h2RemoteServerSettings = databaseSettings
                .getH2RemoteServerSettings();
        H2RemoteServerForm h2RemoteServerForm = form.getH2remoteServerSettings();
        h2RemoteServerForm.setUrl(h2RemoteServerSettings.getUrl());
        h2RemoteServerForm.setUsername(h2RemoteServerSettings.getUsername());
        h2RemoteServerForm.setUsernamePassword(StringUtils.EMPTY);
        h2RemoteServerForm.setFilePassword(StringUtils.EMPTY);
        h2RemoteServerForm.
                setConnectionType(h2RemoteServerSettings.getConnectionType().toString());
        h2RemoteServerForm.setEncryptionAlgorithm(
                h2RemoteServerSettings.getEncryptionAlgorithm().map(Enum::toString)
                        .orElse(ENCRYPTION_DISABLED_STRING));

        PostgresqlSettings postgresqlSettings = databaseSettings.getPostgresqlSettings();
        PostgresqlForm postgresqlForm = form.getPostgresqlSettings();
        postgresqlForm.setHostName(postgresqlSettings.getHostName());
        postgresqlForm.setPort(postgresqlSettings.getPort());
        postgresqlForm.setDatabaseName(postgresqlSettings.getDatabaseName());
        postgresqlForm.setUsername(postgresqlSettings.getUsername());
        postgresqlForm.setPassword(postgresqlSettings.getPassword());

        form.setCurrentDatabaseProviderName(
                databaseSettings.getCurrentDatabaseProvider().toString());

        return form;
    }

    public DatabaseSettings buildDatabaseSettings(DatabaseSettingsForm form,
                                                  DatabaseSettings currentDatabaseSettings) {
        return DatabaseSettings.builder()
                .commonSettings(buildCommon(form))
                .h2InMemorySettings(buildH2InMemoryPart(form))
                .h2EmbeddedSettings(buildH2EmbeddedPart(form, currentDatabaseSettings))
                .h2ManagedServerSettings(buildH2ManagedServerPart(form, currentDatabaseSettings))
                .h2RemoteServerSettings(buildH2RemoteServerPart(form, currentDatabaseSettings))
                .postgresqlSettings(buildPostgresqlPart(form, currentDatabaseSettings))
                .currentDatabaseProvider(getCurrentDatabaseProvider(form))
                .build();
    }

    private CommonDatabaseSettings buildCommon(DatabaseSettingsForm form) {
        return CommonDatabaseSettings.builder()
                .minimumIdleConnections(form.getMinimumIdleConnections())
                .maximumPoolSize(form.getMaximumPoolSize())
                .connectionTimeoutMilliseconds(form.getConnectionTimeoutMilliseconds())
                .connectionMaxLifetimeMilliseconds(form.getConnectionMaxLifetimeMilliseconds())
                .idleTimeoutMilliseconds(form.getIdleTimeoutMilliseconds())
                .validationTimeoutMilliseconds(form.getValidationTimeoutMilliseconds())
                .leakDetectionThresholdMilliseconds(form.getLeakDetectionThresholdMilliseconds())
                .failAtStartingImmediately(form.isFailAtStartingImmediately())
                .dropDatabaseAtStart(form.isDropDatabaseAtStart())
                .auditEnabled(form.isAuditEnabled())
                .build();
    }

    private H2InMemorySettings buildH2InMemoryPart(DatabaseSettingsForm form) {
        return H2InMemorySettings.builder()
                .databaseName(form.getH2inMemorySettings().getDatabaseName())
                .build();
    }

    private H2EmbeddedSettings buildH2EmbeddedPart(DatabaseSettingsForm form,
                                                   DatabaseSettings currentDatabaseSettings) {
        H2EmbeddedSettings currentSettings = currentDatabaseSettings
                .getH2EmbeddedSettings();
        H2EmbeddedForm h2EmbeddedForm = form.getH2embeddedSettings();
        return H2EmbeddedSettings.builder()
                .databaseFileName(h2EmbeddedForm.getDatabaseFileName())
                .username(h2EmbeddedForm.getUsername())
                .usernamePassword(
                        StringUtils.isEmpty(h2EmbeddedForm.getUsernamePassword()) ? currentSettings
                                .getUsernamePassword() : h2EmbeddedForm.getUsernamePassword())
                .filePassword(
                        StringUtils.isEmpty(h2EmbeddedForm.getFilePassword()) ? currentSettings
                                .getFilePassword() : h2EmbeddedForm.getFilePassword())
                .encryptionAlgorithm(
                        ENCRYPTION_DISABLED_STRING.equals(h2EmbeddedForm.getEncryptionAlgorithm()) ?
                                Optional.empty() :
                                Optional.of(H2EncryptionAlgorithm
                                        .valueOf(h2EmbeddedForm.getEncryptionAlgorithm())))
                .build();
    }

    private H2ManagedServerSettings buildH2ManagedServerPart(DatabaseSettingsForm form,
                                                             DatabaseSettings currentDatabaseSettings) {
        H2ManagedServerSettings currentSettings = currentDatabaseSettings
                .getH2ManagedServerSettings();
        H2ManagedServerForm h2ManagedServerForm = form.getH2managedServerSettings();

        H2ManagedServerSettings settings = new H2ManagedServerSettings();
        settings.setDatabaseFileName(h2ManagedServerForm.getDatabaseFileName());
        settings.setPort(h2ManagedServerForm.getPort());
        settings.setUsername(h2ManagedServerForm.getUsername());
        settings.setUsernamePassword(
                StringUtils.isEmpty(h2ManagedServerForm.getUsernamePassword()) ? currentSettings
                        .getUsernamePassword() : h2ManagedServerForm.getUsernamePassword());
        settings.setFilePassword(
                StringUtils.isEmpty(h2ManagedServerForm.getFilePassword()) ? currentSettings
                        .getFilePassword() : h2ManagedServerForm.getFilePassword());
        settings
                .setConnectionType(H2ConnectionType.valueOf(h2ManagedServerForm.getConnectionType()));
        settings.setEncryptionAlgorithm(
                ENCRYPTION_DISABLED_STRING.equals(h2ManagedServerForm.getEncryptionAlgorithm()) ?
                        Optional.empty() :
                        Optional.of(H2EncryptionAlgorithm
                                .valueOf(h2ManagedServerForm.getEncryptionAlgorithm())));
        return settings;
    }

    private H2RemoteServerSettings buildH2RemoteServerPart(DatabaseSettingsForm form,
                                                           DatabaseSettings currentDatabaseSettings) {
        H2RemoteServerSettings currentSettings = currentDatabaseSettings
                .getH2RemoteServerSettings();
        H2RemoteServerForm h2RemoteServerForm = form.getH2remoteServerSettings();

        H2RemoteServerSettings settings = new H2RemoteServerSettings();
        settings.setUrl(h2RemoteServerForm.getUrl());
        settings.setUsername(h2RemoteServerForm.getUsername());
        settings.setUsernamePassword(
                StringUtils.isEmpty(h2RemoteServerForm.getUsernamePassword()) ? currentSettings
                        .getUsernamePassword() : h2RemoteServerForm.getUsernamePassword());
        settings.setFilePassword(
                StringUtils.isEmpty(h2RemoteServerForm.getFilePassword()) ? currentSettings
                        .getFilePassword() : h2RemoteServerForm.getFilePassword());
        settings
                .setConnectionType(H2ConnectionType.valueOf(h2RemoteServerForm.getConnectionType()));
        settings.setEncryptionAlgorithm(
                ENCRYPTION_DISABLED_STRING.equals(h2RemoteServerForm.getEncryptionAlgorithm()) ?
                        Optional.empty() :
                        Optional.of(H2EncryptionAlgorithm
                                .valueOf(h2RemoteServerForm.getEncryptionAlgorithm())));
        return settings;
    }

    private PostgresqlSettings buildPostgresqlPart(DatabaseSettingsForm form,
                                                   DatabaseSettings currentDatabaseSettings) {
        PostgresqlSettings currentSettings = currentDatabaseSettings.getPostgresqlSettings();
        PostgresqlForm postgresqlForm = form.getPostgresqlSettings();
        return PostgresqlSettings.builder()
                .hostName(postgresqlForm.getHostName())
                .port(postgresqlForm.getPort())
                .databaseName(postgresqlForm.getDatabaseName())
                .username(postgresqlForm.getUsername())
                .password(StringUtils.isEmpty(postgresqlForm.getPassword()) ?
                        currentSettings.getPassword() : postgresqlForm.getPassword())
                .build();
    }

    private DatabaseProvider getCurrentDatabaseProvider(DatabaseSettingsForm form) {
        return DatabaseProvider.valueOf(form.getCurrentDatabaseProviderName());
    }

}
