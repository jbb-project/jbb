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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Sets;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.DatabaseConfigException;
import org.jbb.system.api.database.DatabaseSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseSettingsServiceImplTest {
    @Mock
    private DbProperties dbPropertiesMock;

    @Mock
    private DatabaseSettingsFactory databaseSettingsFactoryMock;

    @Mock
    private Validator validatorMock;

    @Mock
    private ReconnectionToDbPropertyListener reconnectionPropertyListenerMock;

    @Mock
    private ConnectionToDatabaseEventSender eventSenderMock;

    @Mock
    private DatabaseSettingsSaver settingsSaverMock;

    @InjectMocks
    private DatabaseSettingsServiceImpl databaseSettingsService;

    @Test
    public void shouldReturnSettings_fromFactory() throws Exception {
        // given
        DatabaseSettings databaseSettingsMock = mock(DatabaseSettings.class);
        given(databaseSettingsFactoryMock.currentDatabaseSettings()).willReturn(databaseSettingsMock);

        // when
        DatabaseSettings result = databaseSettingsService.getDatabaseSettings();

        // then
        assertThat(result).isEqualTo(databaseSettingsMock);
    }

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullSettingsPassed() throws Exception {
        // when
        databaseSettingsService.setDatabaseSettings(null);

        // then
        // throw NullPointerException
    }

    @Test(expected = DatabaseConfigException.class)
    public void shouldThrowDatabaseSettingsException_whenValidationOfSettingsFailed() throws Exception {
        // given
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet(mock(ConstraintViolation.class)));

        // when
        databaseSettingsService.setDatabaseSettings(databaseSettingsMock());

        // then
        // throw DatabaseSettingsException
    }

    @Test
    public void shouldSetProperties_whenValidationOfSettingsPassed() throws Exception {
        // given
        DatabaseSettings databaseSettingsMock = databaseSettingsMock();
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        // when
        databaseSettingsService.setDatabaseSettings(databaseSettingsMock);

        // then
        verify(dbPropertiesMock).removePropertyChangeListener(eq(reconnectionPropertyListenerMock));
        verify(dbPropertiesMock).addPropertyChangeListener(eq(reconnectionPropertyListenerMock));
    }

    private DatabaseSettings databaseSettingsMock() {
        return mock(DatabaseSettings.class);
    }

}