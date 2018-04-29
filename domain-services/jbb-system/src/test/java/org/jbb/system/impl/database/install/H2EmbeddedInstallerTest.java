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
import org.jbb.install.database.H2EmbeddedInstallationData;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2EmbeddedSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class H2EmbeddedInstallerTest {

    @InjectMocks
    private H2EmbeddedInstaller h2EmbeddedInstaller;

    @Test
    public void h2EmbeddedProviderShouldBeApplicable() {
        // when
        boolean applicable = h2EmbeddedInstaller.isApplicable(DatabaseProvider.H2_EMBEDDED);

        // then
        assertThat(applicable).isTrue();
    }

    @Test
    public void installationDataShouldBeAppliedToDatabaseSettings() throws Exception {
        // given
        DatabaseInstallationData databaseInstallationData = DatabaseInstallationData.builder()
            .h2EmbeddedInstallationData(H2EmbeddedInstallationData.builder()
                .databaseFileName("jbb-db")
                .username("jbb")
                .usernamePassword("jbbpass")
                .build())
            .build();

        DatabaseSettings databaseSettings = DatabaseSettings.builder()
            .h2EmbeddedSettings(H2EmbeddedSettings.builder().build())
            .build();

        // when
        h2EmbeddedInstaller.apply(databaseInstallationData, databaseSettings);

        // then
        H2EmbeddedSettings h2EmbeddedSettings = databaseSettings.getH2EmbeddedSettings();
        assertThat(h2EmbeddedSettings.getDatabaseFileName()).isEqualTo("jbb-db");
        assertThat(h2EmbeddedSettings.getUsername()).isEqualTo("jbb");
        assertThat(h2EmbeddedSettings.getUsernamePassword()).isEqualTo("jbbpass");
    }

}