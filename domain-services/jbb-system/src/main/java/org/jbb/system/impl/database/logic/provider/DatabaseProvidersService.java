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

import com.google.common.collect.ImmutableMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseProvidersService {

    private static final Map<String, Class<? extends DatabaseProviderManager>> PROVIDERS =
        ImmutableMap.<String, Class<? extends DatabaseProviderManager>>builder()
            .put(H2InMemoryManager.PROVIDER_PROPERTY_VALUE, H2InMemoryManager.class)
            .put(H2EmbeddedManager.PROVIDER_PROPERTY_VALUE, H2EmbeddedManager.class)
            .put(H2ManagedServerManager.PROVIDER_PROPERTY_VALUE, H2ManagedServerManager.class)
            .put(H2RemoteServerManager.PROVIDER_PROPERTY_VALUE, H2RemoteServerManager.class)
            .put(PostgresqlManager.PROVIDER_PROPERTY_VALUE, PostgresqlManager.class)
            .build();

    private final DbProperties dbProperties;
    private final ApplicationContext applicationContext;

    public DatabaseProviderManager getManagerForCurrentProvider() {
        String providerName = dbProperties.currentProvider();
        return getManagerForProviderName(providerName);
    }

    private DatabaseProviderManager getManagerForProviderName(String providerName) {
        Class managerClass = PROVIDERS.get(providerName.trim().toLowerCase());

        if (managerClass != null) {
            return (DatabaseProviderManager) applicationContext.getBean(managerClass);
        }

        throw new IllegalStateException(
            String.format("No database provider with name: %s", providerName));

    }

    public DatabaseProvider getCurrentDatabaseProvider() {
        return getManagerForCurrentProvider().getProviderName();
    }

    public void setSettingsForAllProviders(DatabaseSettings newDatabaseSettings) {
        PROVIDERS.values().forEach(
            providerManagerClass -> applicationContext.getBean(providerManagerClass)
                .setProviderSettings(newDatabaseSettings)
        );
    }

    public void setNewProvider(DatabaseSettings newDatabaseSettings) {
        DatabaseProvider databaseProviderName = newDatabaseSettings.getCurrentDatabaseProvider();
        String formattedProviderName = databaseProviderName.toString().replaceAll("_", "-").trim()
            .toLowerCase();
        dbProperties.setProperty(DbProperties.DB_CURRENT_PROVIDER, formattedProviderName);
    }
}
