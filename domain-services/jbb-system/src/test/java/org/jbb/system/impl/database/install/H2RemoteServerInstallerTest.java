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
import org.jbb.install.database.H2RemoteServerInstallationData;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2RemoteServerSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class H2RemoteServerInstallerTest {

    @InjectMocks
    private H2RemoteServerInstaller h2RemoteServerInstaller;

    @Test
    public void h2RemoteServerProviderShouldBeApplicable() {
        // when
        boolean applicable = h2RemoteServerInstaller
            .isApplicable(DatabaseProvider.H2_REMOTE_SERVER);

        // then
        assertThat(applicable).isTrue();
    }

    @Test
    public void installationDataShouldBeAppliedToDatabaseSettings() throws Exception {
        // given
        DatabaseInstallationData databaseInstallationData = DatabaseInstallationData.builder()
            .h2RemoteServerInstallationData(H2RemoteServerInstallationData.builder()
                .username("jbb")
                .usernamePassword("jbbpass")
                .url("tcp://localhost:9888")
                .build())
            .build();

        DatabaseSettings databaseSettings = DatabaseSettings.builder()
            .h2RemoteServerSettings(new H2RemoteServerSettings())
            .build();

        // when
        h2RemoteServerInstaller.apply(databaseInstallationData, databaseSettings);

        // then
        H2RemoteServerSettings h2RemoteServerSettings = databaseSettings
            .getH2RemoteServerSettings();
        assertThat(h2RemoteServerSettings.getUsername()).isEqualTo("jbb");
        assertThat(h2RemoteServerSettings.getUsernamePassword()).isEqualTo("jbbpass");
        assertThat(h2RemoteServerSettings.getUrl()).isEqualTo("tcp://localhost:9888");
    }

}