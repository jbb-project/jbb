/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.install;

import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.install.database.H2ManagedServerInstallationData;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.springframework.stereotype.Component;

@Component
public class H2ManagedServerInstaller implements DbProviderInstaller {

    @Override
    public boolean isApplicable(DatabaseProvider databaseProvider) {
        return databaseProvider == DatabaseProvider.H2_MANAGED_SERVER;
    }

    @Override
    public void apply(DatabaseInstallationData dbInstallData, DatabaseSettings databaseSettings) {
        H2ManagedServerSettings h2ManagedServerSettings = databaseSettings
                .getH2ManagedServerSettings();
        H2ManagedServerInstallationData managedServerData = dbInstallData
                .getH2ManagedServerInstallationData();
        h2ManagedServerSettings.setUsername(managedServerData.getUsername());
        h2ManagedServerSettings.setDatabaseFileName(managedServerData.getDatabaseFileName());
        h2ManagedServerSettings.setPort(managedServerData.getPort());
        h2ManagedServerSettings.setUsernamePassword(managedServerData.getUsernamePassword());
        h2ManagedServerSettings.setFilePassword(managedServerData.getUsernamePassword());
    }
}
