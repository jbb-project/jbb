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
import static org.mockito.ArgumentMatchers.nullable;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import com.google.common.collect.Sets;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.CommonDatabaseSettings;
import org.jbb.system.api.database.DatabaseConfigException;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.h2.H2ConnectionType;
import org.jbb.system.api.database.h2.H2EncryptionAlgorithm;
import org.jbb.system.api.database.h2.H2ManagedServerSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseSettingsServiceImplTest {
    @Mock
    private ConnectionToDatabaseEventSender eventSenderMock;
    @Mock
    private DbProperties dbPropertiesMock;
    @Mock
    private DatabaseSettingsFactory databaseSettingsFactoryMock;
    @Mock
    private Validator validatorMock;

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
        H2ManagedServerSettings providerSettings = (H2ManagedServerSettings) databaseSettingsMock
            .getProviderSettings();
        given(providerSettings.getConnectionType()).willReturn(H2ConnectionType.TCP);
        given(providerSettings.getEncryptionAlgorithm()).willReturn(H2EncryptionAlgorithm.AES);
        given(validatorMock.validate(any())).willReturn(Sets.newHashSet());

        // when
        databaseSettingsService.setDatabaseSettings(databaseSettingsMock);

        // then
        verify(dbPropertiesMock, times(17)).setProperty(any(String.class), nullable(String.class));
    }

    private DatabaseSettings databaseSettingsMock() {
        DatabaseSettings databaseSettingsMock = mock(DatabaseSettings.class);
        given(databaseSettingsMock.getCommonSettings())
            .willReturn(mock(CommonDatabaseSettings.class));
        given(databaseSettingsMock.getProviderSettings())
            .willReturn(mock(H2ManagedServerSettings.class));
        return databaseSettingsMock;
    }

}