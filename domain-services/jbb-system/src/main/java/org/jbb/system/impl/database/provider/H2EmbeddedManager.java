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
import org.jbb.lib.db.provider.H2EmbeddedProvider;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2EmbeddedSettings;
import org.jbb.system.api.database.h2.H2EncryptionAlgorithm;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class H2EmbeddedManager implements DatabaseProviderManager<H2EmbeddedSettings> {

    public static final String PROVIDER_PROPERTY_VALUE = H2EmbeddedProvider.PROVIDER_VALUE;

    private final DbProperties dbProperties;

    @Override
    public DatabaseProvider getProviderName() {
        return DatabaseProvider.H2_EMBEDDED;
    }

    @Override
    public H2EmbeddedSettings getCurrentProviderSettings() {
        return H2EmbeddedSettings.builder()
            .databaseFileName(dbProperties.h2EmbeddedDbName())
            .username(dbProperties.h2EmbeddedUsername())
            .usernamePassword(dbProperties.h2EmbeddedPassword())
            .filePassword(dbProperties.h2EmbeddedFilePassword())
            .encryptionAlgorithm(
                StringUtils.isEmpty(dbProperties.h2EmbeddedDbEncryptionAlgorithm()) ?
                    Optional.empty() : Optional.of(H2EncryptionAlgorithm
                    .valueOf(dbProperties.h2EmbeddedDbEncryptionAlgorithm().toUpperCase())))
            .build();
    }

    @Override
    public void setProviderSettings(DatabaseSettings newDatabaseSettings) {
        H2EmbeddedSettings newProviderSettings = newDatabaseSettings
            .getH2EmbeddedSettings();

        dbProperties.setProperty(DbProperties.H2_EMBEDDED_DB_NAME_KEY,
            newProviderSettings.getDatabaseFileName());
        dbProperties.setProperty(DbProperties.H2_EMBEDDED_DB_USERNAME_KEY,
            newProviderSettings.getUsername());
        dbProperties.setProperty(DbProperties.H2_EMBEDDED_DB_PASS_KEY,
            newProviderSettings.getUsernamePassword());
        dbProperties.setProperty(DbProperties.H2_EMBEDDED_DB_FILE_PASS_KEY,
            newProviderSettings.getFilePassword());
        dbProperties.setProperty(DbProperties.H2_EMBEDDED_DB_ENCRYPTION_ALGORITHM_KEY,
            newProviderSettings.getEncryptionAlgorithm()
                .map(encryptionAlgorithm -> encryptionAlgorithm.toString().toLowerCase())
                .orElse(StringUtils.EMPTY));
    }
}
