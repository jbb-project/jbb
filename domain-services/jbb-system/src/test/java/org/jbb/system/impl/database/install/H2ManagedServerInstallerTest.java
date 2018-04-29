/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.install;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.install.database.DatabaseInstallationData;
import org.jbb.install.database.H2ManagedServerInstallationData;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class H2ManagedServerInstallerTest {

    @InjectMocks
    private H2ManagedServerInstaller h2ManagedServerInstaller;

    @Test
    public void h2ManagedServerProviderShouldBeApplicable() {
        // when
        boolean applicable = h2ManagedServerInstaller
            .isApplicable(DatabaseProvider.H2_MANAGED_SERVER);

        // then
        assertThat(applicable).isTrue();
    }

    @Test
    public void installationDataShouldBeAppliedToDatabaseSettings() throws Exception {
        // given
        DatabaseInstallationData databaseInstallationData = DatabaseInstallationData.builder()
            .h2ManagedServerInstallationData(H2ManagedServerInstallationData.builder()
                .databaseFileName("jbb-db")
                .username("jbb")
                .usernamePassword("jbbpass")
                .port(1234)
                .build())
            .build();

        DatabaseSettings databaseSettings = DatabaseSettings.builder()
            .h2ManagedServerSettings(new H2ManagedServerSettings())
            .build();

        // when
        h2ManagedServerInstaller.apply(databaseInstallationData, databaseSettings);

        // then
        H2ManagedServerSettings h2ManagedServerSettings = databaseSettings
            .getH2ManagedServerSettings();
        assertThat(h2ManagedServerSettings.getDatabaseFileName()).isEqualTo("jbb-db");
        assertThat(h2ManagedServerSettings.getUsername()).isEqualTo("jbb");
        assertThat(h2ManagedServerSettings.getUsernamePassword()).isEqualTo("jbbpass");
        assertThat(h2ManagedServerSettings.getPort()).isEqualTo(1234);
    }

}