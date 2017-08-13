/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.logic.provider;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.db.DbProperties;
import org.jbb.lib.db.provider.H2InMemoryProvider;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2EncryptionAlgorithm;
import org.jbb.system.api.database.h2.H2InMemorySettings;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class H2InMemoryManager implements DatabaseProviderManager<H2InMemorySettings> {

    public static final String PROVIDER_PROPERTY_VALUE = H2InMemoryProvider.PROVIDER_VALUE;

    private final DbProperties dbProperties;

    @Override
    public DatabaseProvider getProviderName() {
        return DatabaseProvider.H2_IN_MEMORY;
    }

    @Override
    public H2InMemorySettings getCurrentProviderSettings() {
        return H2InMemorySettings.builder()
            .databaseFileName(dbProperties.h2InMemoryDbName())
            .username(dbProperties.h2InMemoryUsername())
            .usernamePassword(dbProperties.h2InMemoryPassword())
            .filePassword(dbProperties.h2InMemoryFilePassword())
            .encryptionAlgorithm(
                StringUtils.isEmpty(dbProperties.h2InMemoryDbEncryptionAlgorithm()) ?
                    Optional.empty() : Optional.of(H2EncryptionAlgorithm
                    .valueOf(dbProperties.h2InMemoryDbEncryptionAlgorithm().toUpperCase())))
            .build();
    }

    @Override
    public void setProviderSettings(DatabaseSettings newDatabaseSettings) {
        H2InMemorySettings newProviderSettings = newDatabaseSettings
            .getH2InMemorySettings();

        dbProperties.setProperty(DbProperties.H2_IN_MEMORY_DB_NAME_KEY,
            newProviderSettings.getDatabaseFileName());
        dbProperties.setProperty(DbProperties.H2_IN_MEMORY_DB_USERNAME_KEY,
            newProviderSettings.getUsername());
        dbProperties.setProperty(DbProperties.H2_IN_MEMORY_DB_PASS_KEY,
            newProviderSettings.getUsernamePassword());
        dbProperties.setProperty(DbProperties.H2_IN_MEMORY_DB_FILE_PASS_KEY,
            newProviderSettings.getFilePassword());
        dbProperties.setProperty(DbProperties.H2_IN_MEMORY_DB_ENCRYPTION_ALGORITHM_KEY,
            newProviderSettings.getEncryptionAlgorithm()
                .map(encryptionAlgorithm -> encryptionAlgorithm.toString().toLowerCase())
                .orElse(StringUtils.EMPTY));
    }
}
