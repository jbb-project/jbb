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

import static org.mockito.Mockito.verify;

import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.impl.database.logic.provider.DatabaseProvidersService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseSettingsSaverTest {

    @Mock
    private CommonDatabaseSettingsManager commonSettingsManagerMock;

    @Mock
    private DatabaseProvidersService databaseProvidersServiceMock;

    @InjectMocks
    private DatabaseSettingsSaver databaseSettingsSaver;

    @Test
    public void shouldDelegateSettingAllNewSettings() throws Exception {
        // given
        DatabaseSettings databaseSettings = DatabaseSettings.builder().build();

        // when
        databaseSettingsSaver.setDatabaseSettings(databaseSettings);

        // then
        verify(commonSettingsManagerMock).setCommonSettings(databaseSettings);
        verify(databaseProvidersServiceMock).setNewProvider(databaseSettings);
        verify(databaseProvidersServiceMock).setSettingsForAllProviders(databaseSettings);
    }
}