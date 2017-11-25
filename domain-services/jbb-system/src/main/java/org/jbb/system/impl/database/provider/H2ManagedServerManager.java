/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.provider;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.db.DbProperties;
import org.jbb.lib.db.provider.H2ManagedServerProvider;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2ConnectionType;
import org.jbb.system.api.database.h2.H2EncryptionAlgorithm;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class H2ManagedServerManager implements DatabaseProviderManager<H2ManagedServerSettings> {

    public static final String PROVIDER_PROPERTY_VALUE = H2ManagedServerProvider.PROVIDER_VALUE;

    private final DbProperties dbProperties;

    @Override
    public DatabaseProvider getProviderName() {
        return DatabaseProvider.H2_MANAGED_SERVER;
    }

    @Override
    public H2ManagedServerSettings getCurrentProviderSettings() {
        H2ManagedServerSettings settings = new H2ManagedServerSettings();
        settings.setDatabaseFileName(dbProperties.h2ManagedServerDbName());
        settings.setPort(dbProperties.h2ManagedServerDbPort());
        settings.setUsername(dbProperties.h2ManagedServerUsername());
        settings.setUsernamePassword(dbProperties.h2ManagedServerPassword());
        settings.setFilePassword(dbProperties.h2ManagedServerFilePassword());
        settings.setConnectionType(H2ConnectionType
            .valueOf(dbProperties.h2ManagedServerConnectionType().toUpperCase()));
        settings.setEncryptionAlgorithm(
            StringUtils.isEmpty(dbProperties.h2ManagedServerDbEncryptionAlgorithm()) ?
                Optional.empty() : Optional.of(H2EncryptionAlgorithm
                .valueOf(dbProperties.h2ManagedServerDbEncryptionAlgorithm().toUpperCase())));
        return settings;
    }

    @Override
    public void setProviderSettings(DatabaseSettings newDatabaseSettings) {
        H2ManagedServerSettings newProviderSettings = newDatabaseSettings
            .getH2ManagedServerSettings();

        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_NAME_KEY,
            newProviderSettings.getDatabaseFileName());
        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_PORT_KEY,
            Integer.toString(newProviderSettings.getPort()));
        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_USERNAME_KEY,
            newProviderSettings.getUsername());
        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_PASS_KEY,
            newProviderSettings.getUsernamePassword());
        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_FILE_PASS_KEY,
            newProviderSettings.getFilePassword());
        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_CONNECTION_TYPE_KEY,
            newProviderSettings.getConnectionType().toString().toLowerCase());
        dbProperties.setProperty(DbProperties.H2_MANAGED_SERVER_DB_ENCRYPTION_ALGORITHM_KEY,
            newProviderSettings.getEncryptionAlgorithm()
                .map(encryptionAlgorithm -> encryptionAlgorithm.toString().toLowerCase())
                .orElse(StringUtils.EMPTY));
    }

}
