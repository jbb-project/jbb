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
import org.jbb.install.database.PostgresqlInstallationData;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.postgres.PostgresqlSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PostgresqlInstallerTest {

    @InjectMocks
    private PostgresqlInstaller postgresqlInstaller;

    @Test
    public void postgresqlProviderShouldBeApplicable() {
        // when
        boolean applicable = postgresqlInstaller.isApplicable(DatabaseProvider.POSTGRESQL);

        // then
        assertThat(applicable).isTrue();
    }

    @Test
    public void installationDataShouldBeAppliedToDatabaseSettings() throws Exception {
        // given
        DatabaseInstallationData databaseInstallationData = DatabaseInstallationData.builder()
            .postgresqlInstallationData(PostgresqlInstallationData.builder()
                .hostName("localhost")
                .databaseName("jbbdb")
                .port(5432)
                .username("dba")
                .password("changeit")
                .build())
            .build();

        DatabaseSettings databaseSettings = DatabaseSettings.builder()
            .postgresqlSettings(PostgresqlSettings.builder().build())
            .build();

        // when
        postgresqlInstaller.apply(databaseInstallationData, databaseSettings);

        // then
        PostgresqlSettings postgresqlSettings = databaseSettings.getPostgresqlSettings();
        assertThat(postgresqlSettings.getHostName()).isEqualTo("localhost");
        assertThat(postgresqlSettings.getDatabaseName()).isEqualTo("jbbdb");
        assertThat(postgresqlSettings.getPort()).isEqualTo(5432);
        assertThat(postgresqlSettings.getUsername()).isEqualTo("dba");
        assertThat(postgresqlSettings.getPassword()).isEqualTo("changeit");
    }

}