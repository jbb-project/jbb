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

import java.util.Optional;
import org.apache.commons.lang3.StringUtils;
import org.jbb.system.api.database.CommonDatabaseSettings;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2ConnectionType;
import org.jbb.system.api.database.h2.H2EncryptionAlgorithm;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.jbb.system.web.database.form.DatabaseSettingsForm;
import org.jbb.system.web.database.form.H2ManagedServerForm;
import org.springframework.stereotype.Component;

@Component
public class FormDatabaseTranslator {

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
        h2managedServerForm
            .setEncryptionAlgorithm(h2ManagedServerSettings.getEncryptionAlgorithm().isPresent() ?
                h2ManagedServerSettings.getEncryptionAlgorithm().get().toString() :
                "NONE"
            );

        form.setCurrentDatabaseProviderName(
            databaseSettings.getCurrentDatabaseProvider().toString());

        return form;
    }

    public DatabaseSettings buildDatabaseSettings(DatabaseSettingsForm form,
        DatabaseSettings currentDatabaseSettings) {
        return DatabaseSettings.builder()
            .commonSettings(buildCommon(form))
            .h2ManagedServerSettings(buildH2ManagedServerPart(form, currentDatabaseSettings))
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

    private H2ManagedServerSettings buildH2ManagedServerPart(DatabaseSettingsForm form,
        DatabaseSettings currentDatabaseSettings) {
        H2ManagedServerSettings currentSettings = currentDatabaseSettings
            .getH2ManagedServerSettings();
        H2ManagedServerForm h2ManagedServerForm = form.getH2managedServerSettings();
        return H2ManagedServerSettings.builder()
            .databaseFileName(h2ManagedServerForm.getDatabaseFileName())
            .port(h2ManagedServerForm.getPort())
            .username(h2ManagedServerForm.getUsername())
            .usernamePassword(
                StringUtils.isEmpty(h2ManagedServerForm.getUsernamePassword()) ? currentSettings
                    .getUsernamePassword() : h2ManagedServerForm.getUsernamePassword())
            .filePassword(
                StringUtils.isEmpty(h2ManagedServerForm.getFilePassword()) ? currentSettings
                    .getFilePassword() : h2ManagedServerForm.getFilePassword())
            .connectionType(H2ConnectionType.valueOf(h2ManagedServerForm.getConnectionType()))
            .encryptionAlgorithm("NONE".equals(h2ManagedServerForm.getEncryptionAlgorithm()) ?
                Optional.empty() :
                Optional.of(H2EncryptionAlgorithm
                    .valueOf(h2ManagedServerForm.getEncryptionAlgorithm())))
            .build();
    }

    private DatabaseProvider getCurrentDatabaseProvider(DatabaseSettingsForm form) {
        return DatabaseProvider.valueOf(form.getCurrentDatabaseProviderName());
    }

}
