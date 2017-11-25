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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.jbb.system.api.database.CommonDatabaseSettings;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2EmbeddedSettings;
import org.jbb.system.api.database.h2.H2InMemorySettings;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.jbb.system.api.database.h2.H2RemoteServerSettings;
import org.jbb.system.api.database.postgres.PostgresqlSettings;
import org.jbb.system.impl.database.provider.DatabaseProvidersService;
import org.jbb.system.impl.database.provider.H2EmbeddedManager;
import org.jbb.system.impl.database.provider.H2InMemoryManager;
import org.jbb.system.impl.database.provider.H2ManagedServerManager;
import org.jbb.system.impl.database.provider.H2RemoteServerManager;
import org.jbb.system.impl.database.provider.PostgresqlManager;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseSettingsFactoryTest {
    @Mock
    private CommonDatabaseSettingsManager commonSettingsManagerMock;

    @Mock
    private DatabaseProvidersService dbProviderServiceMock;

    @Mock
    private H2InMemoryManager h2InMemoryManagerMock;

    @Mock
    private H2EmbeddedManager h2EmbeddedManagerMock;

    @Mock
    private H2ManagedServerManager h2ManagedServerManagerMock;

    @Mock
    private H2RemoteServerManager h2RemoteServerManagerMock;

    @Mock
    private PostgresqlManager postgresqlManagerMock;

    @InjectMocks
    private DatabaseSettingsFactory databaseSettingsFactory;

    @Test
    public void shouldReturnCurrentSettings() throws Exception {
        // given
        CommonDatabaseSettings commonDatabaseSettingsMock = mock(CommonDatabaseSettings.class);
        H2InMemorySettings h2InMemorySettingsMock = mock(H2InMemorySettings.class);
        H2EmbeddedSettings h2EmbeddedSettingsMock = mock(H2EmbeddedSettings.class);
        H2ManagedServerSettings h2ManagedServerSettingsMock = mock(H2ManagedServerSettings.class);
        H2RemoteServerSettings h2RemoteServerSettingsMock = mock(H2RemoteServerSettings.class);
        PostgresqlSettings postgresqlSettingsMock = mock(PostgresqlSettings.class);

        given(commonSettingsManagerMock.getCurrentCommonDatabaseSettings())
            .willReturn(commonDatabaseSettingsMock);
        given(h2InMemoryManagerMock.getCurrentProviderSettings())
            .willReturn(h2InMemorySettingsMock);
        given(h2EmbeddedManagerMock.getCurrentProviderSettings())
            .willReturn(h2EmbeddedSettingsMock);
        given(h2ManagedServerManagerMock.getCurrentProviderSettings())
            .willReturn(h2ManagedServerSettingsMock);
        given(h2RemoteServerManagerMock.getCurrentProviderSettings())
            .willReturn(h2RemoteServerSettingsMock);
        given(postgresqlManagerMock.getCurrentProviderSettings())
            .willReturn(postgresqlSettingsMock);
        given(dbProviderServiceMock.getCurrentDatabaseProvider())
            .willReturn(DatabaseProvider.H2_EMBEDDED);

        // when
        DatabaseSettings databaseSettings = databaseSettingsFactory.currentDatabaseSettings();

        // then
        assertThat(databaseSettings.getCommonSettings()).isEqualTo(commonDatabaseSettingsMock);
        assertThat(databaseSettings.getH2ManagedServerSettings())
            .isEqualTo(h2ManagedServerSettingsMock);
        assertThat(databaseSettings.getCurrentDatabaseProvider())
            .isEqualTo(DatabaseProvider.H2_EMBEDDED);
    }
}