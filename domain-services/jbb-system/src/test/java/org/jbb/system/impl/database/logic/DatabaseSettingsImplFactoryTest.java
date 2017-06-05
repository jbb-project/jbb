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

import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.DatabaseSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseSettingsImplFactoryTest {
    @Mock
    private DbProperties dbPropertiesMock;

    @InjectMocks
    private DatabaseSettingsImplFactory databaseSettingsImplFactory;

    @Test
    public void shouldCreateNew_fromPassed() throws Exception {
        // given
        DatabaseSettings databaseSettings = mock(DatabaseSettings.class);
        given(databaseSettings.getDatabaseFileName()).willReturn("jbb.db");
        given(databaseSettings.getMinimumIdleConnections()).willReturn(5);
        given(databaseSettings.getMaximumPoolSize()).willReturn(10);
        given(databaseSettings.getConnectionTimeoutMilliseconds()).willReturn(1000);
        given(databaseSettings.isFailAtStartingImmediately()).willReturn(true);
        given(databaseSettings.isDropDatabaseAtStart()).willReturn(false);
        given(databaseSettings.isAuditEnabled()).willReturn(true);

        // when
        DatabaseSettings generatedDatabaseSettings =
                databaseSettingsImplFactory.create(databaseSettings);

        // then
        assertThat(generatedDatabaseSettings.getDatabaseFileName()).isEqualTo("jbb.db");
        assertThat(generatedDatabaseSettings.getMinimumIdleConnections()).isEqualTo(5);
        assertThat(generatedDatabaseSettings.getMaximumPoolSize()).isEqualTo(10);
        assertThat(generatedDatabaseSettings.getConnectionTimeoutMilliseconds()).isEqualTo(1000);
        assertThat(generatedDatabaseSettings.isFailAtStartingImmediately()).isTrue();
        assertThat(generatedDatabaseSettings.isDropDatabaseAtStart()).isFalse();
        assertThat(generatedDatabaseSettings.isAuditEnabled()).isTrue();
    }

    @Test
    public void shouldReturnCurrentSettings() throws Exception {
        // given
        given(dbPropertiesMock.dbFilename()).willReturn("jbb.db");
        given(dbPropertiesMock.minimumIdle()).willReturn(5);
        given(dbPropertiesMock.maxPool()).willReturn(10);
        given(dbPropertiesMock.connectionTimeoutMiliseconds()).willReturn(1000);
        given(dbPropertiesMock.failFastDuringInit()).willReturn(true);
        given(dbPropertiesMock.dropDbDuringStart()).willReturn(false);
        given(dbPropertiesMock.auditEnabled()).willReturn(true);

        // when
        DatabaseSettings databaseSettings = databaseSettingsImplFactory.currentDatabaseSettings();

        // then
        assertThat(databaseSettings.getDatabaseFileName()).isEqualTo("jbb.db");
        assertThat(databaseSettings.getMinimumIdleConnections()).isEqualTo(5);
        assertThat(databaseSettings.getMaximumPoolSize()).isEqualTo(10);
        assertThat(databaseSettings.getConnectionTimeoutMilliseconds()).isEqualTo(1000);
        assertThat(databaseSettings.isFailAtStartingImmediately()).isTrue();
        assertThat(databaseSettings.isDropDatabaseAtStart()).isFalse();
        assertThat(databaseSettings.isAuditEnabled()).isTrue();
    }
}