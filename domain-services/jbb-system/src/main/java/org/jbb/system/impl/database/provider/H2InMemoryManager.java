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

import org.jbb.lib.db.DbProperties;
import org.jbb.lib.db.provider.H2InMemoryProvider;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2InMemorySettings;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

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
                .databaseName(dbProperties.h2InMemoryDbName())
                .build();
    }

    @Override
    public void setProviderSettings(DatabaseSettings newDatabaseSettings) {
        H2InMemorySettings newProviderSettings = newDatabaseSettings
                .getH2InMemorySettings();

        dbProperties.setProperty(DbProperties.H2_IN_MEMORY_DB_NAME_KEY,
                newProviderSettings.getDatabaseName());
    }
}
