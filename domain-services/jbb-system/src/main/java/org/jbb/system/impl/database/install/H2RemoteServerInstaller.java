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
import org.jbb.install.database.H2RemoteServerInstallationData;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2RemoteServerSettings;
import org.springframework.stereotype.Component;

@Component
public class H2RemoteServerInstaller implements DbProviderInstaller {

    @Override
    public boolean isApplicable(DatabaseProvider databaseProvider) {
        return databaseProvider == DatabaseProvider.H2_REMOTE_SERVER;
    }

    @Override
    public void apply(DatabaseInstallationData dbInstallData, DatabaseSettings databaseSettings) {
        H2RemoteServerSettings h2RemoteSettings = databaseSettings.getH2RemoteServerSettings();
        H2RemoteServerInstallationData remoteData = dbInstallData
                .getH2RemoteServerInstallationData();
        h2RemoteSettings.setUsername(remoteData.getUsername());
        h2RemoteSettings.setUrl(remoteData.getUrl());
        h2RemoteSettings.setUsernamePassword(remoteData.getUsernamePassword());
        h2RemoteSettings.setFilePassword(remoteData.getUsernamePassword());
    }
}
