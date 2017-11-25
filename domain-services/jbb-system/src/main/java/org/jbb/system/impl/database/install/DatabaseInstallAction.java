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

import com.github.zafarkhaja.semver.Version;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.EnumUtils;
import org.jbb.install.InstallUpdateAction;
import org.jbb.install.InstallationData;
import org.jbb.install.JbbVersions;
import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.DatabaseSettingsService;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DatabaseInstallAction implements InstallUpdateAction {

    private final List<DbProviderInstaller> installers;

    private final DatabaseSettingsService databaseSettingsService;

    @Override
    public Version fromVersion() {
        return JbbVersions.VERSION_0_3_0;
    }

    @Override
    public void install(InstallationData installationData) {
        DatabaseInstallationData dbInstallData = installationData.getDatabaseInstallationData();
        DatabaseSettings databaseSettings = databaseSettingsService.getDatabaseSettings();
        DatabaseProvider databaseProvider = EnumUtils
            .getEnum(DatabaseProvider.class, dbInstallData.getDatabaseType().toString());
        databaseSettings.setCurrentDatabaseProvider(databaseProvider);

        for (DbProviderInstaller installer : installers) {
            if (installer.isApplicable(databaseProvider)) {
                installer.apply(dbInstallData, databaseSettings);
                break;
            }
        }

        databaseSettingsService.setDatabaseSettings(databaseSettings);
    }
}
