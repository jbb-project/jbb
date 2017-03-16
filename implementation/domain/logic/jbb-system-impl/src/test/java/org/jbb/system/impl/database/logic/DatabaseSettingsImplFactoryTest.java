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
import org.jbb.system.api.model.DatabaseSettings;
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
        given(databaseSettings.databaseFileName()).willReturn("jbb.db");
        given(databaseSettings.minimumIdleConnections()).willReturn(5);
        given(databaseSettings.maximumPoolSize()).willReturn(10);
        given(databaseSettings.connectionTimeoutMilliseconds()).willReturn(1000);
        given(databaseSettings.failAtStartingImmediately()).willReturn(true);
        given(databaseSettings.dropDatabaseAtStart()).willReturn(false);

        // when
        DatabaseSettings generatedDatabaseSettings =
                databaseSettingsImplFactory.create(databaseSettings);

        // then
        assertThat(generatedDatabaseSettings.databaseFileName()).isEqualTo("jbb.db");
        assertThat(generatedDatabaseSettings.minimumIdleConnections()).isEqualTo(5);
        assertThat(generatedDatabaseSettings.maximumPoolSize()).isEqualTo(10);
        assertThat(generatedDatabaseSettings.connectionTimeoutMilliseconds()).isEqualTo(1000);
        assertThat(generatedDatabaseSettings.failAtStartingImmediately()).isTrue();
        assertThat(generatedDatabaseSettings.dropDatabaseAtStart()).isFalse();
    }

    @Test
    public void shouldReturnAcctualSettings() throws Exception {
        // given
        given(dbPropertiesMock.dbFilename()).willReturn("jbb.db");
        given(dbPropertiesMock.minimumIdle()).willReturn(5);
        given(dbPropertiesMock.maxPool()).willReturn(10);
        given(dbPropertiesMock.connectionTimeoutMiliseconds()).willReturn(1000);
        given(dbPropertiesMock.failFastDuringInit()).willReturn(true);
        given(dbPropertiesMock.dropDbDuringStart()).willReturn(false);

        // when
        DatabaseSettings databaseSettings = databaseSettingsImplFactory.currentDatabaseSettings();

        // then
        assertThat(databaseSettings.databaseFileName()).isEqualTo("jbb.db");
        assertThat(databaseSettings.minimumIdleConnections()).isEqualTo(5);
        assertThat(databaseSettings.maximumPoolSize()).isEqualTo(10);
        assertThat(databaseSettings.connectionTimeoutMilliseconds()).isEqualTo(1000);
        assertThat(databaseSettings.failAtStartingImmediately()).isTrue();
        assertThat(databaseSettings.dropDatabaseAtStart()).isFalse();
    }
}