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
import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.CommonDatabaseSettings;
import org.jbb.system.api.database.DatabaseProviderSettings;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2ConnectionType;
import org.jbb.system.api.database.h2.H2EncryptionAlgorithm;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSettingsFactory {
    private final DbProperties dbProperties;

    public DatabaseSettings currentDatabaseSettings() {
        return DatabaseSettings.builder()
            .commonSettings(currentCommonSettings())
            .providerSettings(currentProviderSettings())
            .build();
    }

    private CommonDatabaseSettings currentCommonSettings() {
        return CommonDatabaseSettings.builder()
            .minimumIdleConnections(dbProperties.minimumIdle())
            .maximumPoolSize(dbProperties.maxPool())
            .connectionTimeoutMilliseconds(dbProperties.connectionTimeoutMilliseconds())
            .connectionMaxLifetimeMilliseconds(dbProperties.connectionMaxLifetimeMilliseconds())
            .idleTimeoutMilliseconds(dbProperties.idleTimeoutMilliseconds())
            .validationTimeoutMilliseconds(dbProperties.validationTimeoutMilliseconds())
            .leakDetectionThresholdMilliseconds(dbProperties.leakDetectionThresholdMilliseconds())
            .failAtStartingImmediately(dbProperties.failFastDuringInit())
            .dropDatabaseAtStart(dbProperties.dropDbDuringStart())
            .auditEnabled(dbProperties.auditEnabled())
            .build();
    }

    private DatabaseProviderSettings currentProviderSettings() {
        return new ProviderResolver().resolveProviderFactory().getCurrentProviderSettings();
    }

    private interface ProviderSettingsFactory {

        DatabaseProviderSettings getCurrentProviderSettings();
    }

    private class ProviderResolver {

        ProviderSettingsFactory resolveProviderFactory() {
            String provider = dbProperties.currentProvider();

            if ("h2-managed-server".equalsIgnoreCase(provider)) {
                return new H2ManagedServerSettingsFactory();
            }

            return null;
        }
    }

    private class H2ManagedServerSettingsFactory implements ProviderSettingsFactory {

        @Override
        public DatabaseProviderSettings getCurrentProviderSettings() {
            return H2ManagedServerSettings.builder()
                .databaseFileName(dbProperties.h2ManagedServerDbName())
                .port(Integer.valueOf(dbProperties.h2ManagedServerDbPort()))
                .username(dbProperties.h2ManagedServerUsername())
                .usernamePassword(dbProperties.h2ManagedServerPassword())
                .filePassword(dbProperties.h2ManagedServerFilePassword())
                .connectionType(H2ConnectionType
                    .valueOf(dbProperties.h2ManagedServerConnectionType().toUpperCase()))
                .encryptionAlgorithm(H2EncryptionAlgorithm
                    .valueOf(dbProperties.h2ManagedServerDbEncryptionAlgorithm().toUpperCase()))
                .build();
        }
    }
}
