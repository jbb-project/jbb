/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.logic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.jbb.system.api.database.CommonDatabaseSettings;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.jbb.system.impl.database.logic.provider.DatabaseProvidersService;
import org.jbb.system.impl.database.logic.provider.H2ManagedServerManager;
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
    private H2ManagedServerManager h2ManagedServerServiceMock;

    @InjectMocks
    private DatabaseSettingsFactory databaseSettingsFactory;

    @Test
    public void shouldReturnCurrentSettings() throws Exception {
        // given
        CommonDatabaseSettings commonDatabaseSettingsMock = mock(CommonDatabaseSettings.class);
        H2ManagedServerSettings h2ManagedServerSettingsMock = mock(H2ManagedServerSettings.class);

        given(commonSettingsManagerMock.getCurrentCommonDatabaseSettings())
            .willReturn(commonDatabaseSettingsMock);
        given(h2ManagedServerServiceMock.getCurrentProviderSettings())
            .willReturn(h2ManagedServerSettingsMock);
        given(dbProviderServiceMock.getCurrentDatabaseProvider())
            .willReturn(DatabaseProvider.H2_IN_MEMORY);

        // when
        DatabaseSettings databaseSettings = databaseSettingsFactory.currentDatabaseSettings();

        // then
        assertThat(databaseSettings.getCommonSettings()).isEqualTo(commonDatabaseSettingsMock);
        assertThat(databaseSettings.getH2ManagedServerSettings())
            .isEqualTo(h2ManagedServerSettingsMock);
        assertThat(databaseSettings.getCurrentDatabaseProvider())
            .isEqualTo(DatabaseProvider.H2_IN_MEMORY);
    }
}