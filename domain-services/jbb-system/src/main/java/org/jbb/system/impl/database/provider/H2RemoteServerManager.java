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

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.db.DbProperties;
import org.jbb.lib.db.provider.H2RemoteServerProvider;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2ConnectionType;
import org.jbb.system.api.database.h2.H2EncryptionAlgorithm;
import org.jbb.system.api.database.h2.H2RemoteServerSettings;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class H2RemoteServerManager implements DatabaseProviderManager<H2RemoteServerSettings> {

    public static final String PROVIDER_PROPERTY_VALUE = H2RemoteServerProvider.PROVIDER_VALUE;

    private final DbProperties dbProperties;

    @Override
    public DatabaseProvider getProviderName() {
        return DatabaseProvider.H2_REMOTE_SERVER;
    }

    @Override
    public H2RemoteServerSettings getCurrentProviderSettings() {
        H2RemoteServerSettings settings = new H2RemoteServerSettings();
        settings.setUrl(dbProperties.h2RemoteServerDbUrl());
        settings.setUsername(dbProperties.h2RemoteServerUsername());
        settings.setUsernamePassword(dbProperties.h2RemoteServerPassword());
        settings.setFilePassword(dbProperties.h2RemoteServerFilePassword());
        settings.setConnectionType(H2ConnectionType
                .valueOf(dbProperties.h2RemoteServerConnectionType().toUpperCase()));
        settings.setEncryptionAlgorithm(
                StringUtils.isEmpty(dbProperties.h2RemoteServerDbEncryptionAlgorithm()) ?
                        Optional.empty() : Optional.of(H2EncryptionAlgorithm
                        .valueOf(dbProperties.h2RemoteServerDbEncryptionAlgorithm().toUpperCase())));
        return settings;
    }

    @Override
    public void setProviderSettings(DatabaseSettings newDatabaseSettings) {
        H2RemoteServerSettings newProviderSettings = newDatabaseSettings
                .getH2RemoteServerSettings();

        dbProperties.setProperty(DbProperties.H2_REMOTE_SERVER_DB_URL_KEY,
                newProviderSettings.getUrl());
        dbProperties.setProperty(DbProperties.H2_REMOTE_SERVER_DB_USERNAME_KEY,
                newProviderSettings.getUsername());
        dbProperties.setProperty(DbProperties.H2_REMOTE_SERVER_DB_PASS_KEY,
                newProviderSettings.getUsernamePassword());
        dbProperties.setProperty(DbProperties.H2_REMOTE_SERVER_DB_FILE_PASS_KEY,
                newProviderSettings.getFilePassword());
        dbProperties.setProperty(DbProperties.H2_REMOTE_SERVER_DB_CONNECTION_TYPE_KEY,
                newProviderSettings.getConnectionType().toString().toLowerCase());
        dbProperties.setProperty(DbProperties.H2_REMOTE_SERVER_DB_ENCRYPTION_ALGORITHM_KEY,
                newProviderSettings.getEncryptionAlgorithm()
                        .map(encryptionAlgorithm -> encryptionAlgorithm.toString().toLowerCase())
                        .orElse(StringUtils.EMPTY));
    }
}
