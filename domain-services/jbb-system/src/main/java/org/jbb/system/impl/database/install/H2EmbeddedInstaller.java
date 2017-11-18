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
import org.jbb.install.database.H2EmbeddedInstallationData;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2EmbeddedSettings;
import org.springframework.stereotype.Component;

@Component
public class H2EmbeddedInstaller implements DbProviderInstaller {

    @Override
    public boolean isApplicable(DatabaseProvider databaseProvider) {
        return databaseProvider == DatabaseProvider.H2_EMBEDDED;
    }

    @Override
    public void apply(DatabaseInstallationData dbInstallData, DatabaseSettings databaseSettings) {
        H2EmbeddedSettings h2EmbeddedSettings = databaseSettings.getH2EmbeddedSettings();
        H2EmbeddedInstallationData embeddedData = dbInstallData.getH2EmbeddedInstallationData();
        h2EmbeddedSettings.setUsername(embeddedData.getUsername());
        h2EmbeddedSettings.setDatabaseFileName(embeddedData.getDatabaseFileName());
        h2EmbeddedSettings.setUsernamePassword(embeddedData.getUsernamePassword());
        h2EmbeddedSettings.setFilePassword(embeddedData.getUsernamePassword());
    }

}
