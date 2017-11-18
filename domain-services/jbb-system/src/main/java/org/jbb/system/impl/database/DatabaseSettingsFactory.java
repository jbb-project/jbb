/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database;

import lombok.RequiredArgsConstructor;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.impl.database.provider.DatabaseProvidersService;
import org.jbb.system.impl.database.provider.H2EmbeddedManager;
import org.jbb.system.impl.database.provider.H2InMemoryManager;
import org.jbb.system.impl.database.provider.H2ManagedServerManager;
import org.jbb.system.impl.database.provider.H2RemoteServerManager;
import org.jbb.system.impl.database.provider.PostgresqlManager;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseSettingsFactory {

    private final CommonDatabaseSettingsManager commonSettingsManager;
    private final DatabaseProvidersService dbProviderService;

    private final H2InMemoryManager h2InMemoryManager;
    private final H2EmbeddedManager h2EmbeddedManager;
    private final H2ManagedServerManager h2ManagedServerManager;
    private final H2RemoteServerManager h2RemoteServerManager;
    private final PostgresqlManager postgresqlManager;

    public DatabaseSettings currentDatabaseSettings() {
        return DatabaseSettings.builder()
            .commonSettings(commonSettingsManager.getCurrentCommonDatabaseSettings())
            .h2InMemorySettings(h2InMemoryManager.getCurrentProviderSettings())
            .h2EmbeddedSettings(h2EmbeddedManager.getCurrentProviderSettings())
            .h2ManagedServerSettings(h2ManagedServerManager.getCurrentProviderSettings())
            .h2RemoteServerSettings(h2RemoteServerManager.getCurrentProviderSettings())
            .postgresqlSettings(postgresqlManager.getCurrentProviderSettings())
            .currentDatabaseProvider(dbProviderService.getCurrentDatabaseProvider())
            .build();
    }

}
