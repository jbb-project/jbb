/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.provider;

import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class DatabaseProvidersServiceTest {

    @Mock
    private DbProperties dbPropertiesMock;

    @Mock
    private ApplicationContext applicationContextMock;

    @InjectMocks
    private DatabaseProvidersService databaseProvidersService;

    @Test
    public void shouldGetManagerForCurrentProvider_whenH2InMemory() throws Exception {
        // given
        setUpAppContext();
        given(dbPropertiesMock.currentProvider()).willReturn("h2-in-memory");

        // when
        DatabaseProviderManager databaseProviderManager = databaseProvidersService
                .getManagerForCurrentProvider();

        // then
        assertThat(databaseProviderManager).isInstanceOf(H2InMemoryManager.class);
    }

    @Test
    public void shouldGetManagerForCurrentProvider_whenH2Embedded() throws Exception {
        // given
        setUpAppContext();
        given(dbPropertiesMock.currentProvider()).willReturn("h2-embedded");

        // when
        DatabaseProviderManager databaseProviderManager = databaseProvidersService
                .getManagerForCurrentProvider();

        // then
        assertThat(databaseProviderManager).isInstanceOf(H2EmbeddedManager.class);
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowISE_whenDummyProviderName() throws Exception {
        // given
        given(dbPropertiesMock.currentProvider()).willReturn("dummy-provider");

        // when
        databaseProvidersService.getManagerForCurrentProvider();

        // then
        // throw IllegalStateException
    }

    @Test
    public void shouldGetManagerForCurrentProvider_whenH2ManagedServer() throws Exception {
        // given
        setUpAppContext();
        given(dbPropertiesMock.currentProvider()).willReturn("h2-managed-server");

        // when
        DatabaseProviderManager databaseProviderManager = databaseProvidersService
                .getManagerForCurrentProvider();

        // then
        assertThat(databaseProviderManager).isInstanceOf(H2ManagedServerManager.class);
    }

    @Test
    public void shouldGetManagerForCurrentProvider_whenH2RemoteServer() throws Exception {
        // given
        setUpAppContext();
        given(dbPropertiesMock.currentProvider()).willReturn("h2-remote-server");

        // when
        DatabaseProviderManager databaseProviderManager = databaseProvidersService
                .getManagerForCurrentProvider();

        // then
        assertThat(databaseProviderManager).isInstanceOf(H2RemoteServerManager.class);
    }

    @Test
    public void shouldGetProvider_whenH2InMemory() throws Exception {
        // given
        H2InMemoryManager h2InMemoryManager = mock(H2InMemoryManager.class);
        given(h2InMemoryManager.getProviderName()).willReturn(DatabaseProvider.H2_IN_MEMORY);
        given(applicationContextMock.getBean(H2InMemoryManager.class))
                .willReturn(h2InMemoryManager);
        given(dbPropertiesMock.currentProvider()).willReturn("h2-in-memory");

        // when
        DatabaseProvider databaseProvider = databaseProvidersService.getCurrentDatabaseProvider();

        // then
        assertThat(databaseProvider).isEqualTo(DatabaseProvider.H2_IN_MEMORY);
    }

    @Test
    public void shouldDelegateSettingsToManagers() throws Exception {
        // given
        setUpAppContext();
        DatabaseSettings databaseSettings = mock(DatabaseSettings.class);

        // when
        databaseProvidersService.setSettingsForAllProviders(databaseSettings);

        // then
        verify(applicationContextMock.getBean(H2InMemoryManager.class))
                .setProviderSettings(eq(databaseSettings));
        verify(applicationContextMock.getBean(H2EmbeddedManager.class))
                .setProviderSettings(eq(databaseSettings));
        verify(applicationContextMock.getBean(H2ManagedServerManager.class))
                .setProviderSettings(eq(databaseSettings));
        verify(applicationContextMock.getBean(H2RemoteServerManager.class))
                .setProviderSettings(eq(databaseSettings));
        verify(applicationContextMock.getBean(PostgresqlManager.class))
                .setProviderSettings(eq(databaseSettings));
    }

    @Test
    public void shouldSetNewDatabaseProvider() throws Exception {
        // given
        DatabaseSettings databaseSettings = DatabaseSettings.builder()
                .currentDatabaseProvider(DatabaseProvider.H2_MANAGED_SERVER)
                .build();

        // when
        databaseProvidersService.setNewProvider(databaseSettings);

        // then
        verify(dbPropertiesMock)
                .setProperty(eq(DbProperties.DB_CURRENT_PROVIDER), eq("h2-managed-server"));
    }

    private void setUpAppContext() {
        given(applicationContextMock.getBean(H2InMemoryManager.class))
                .willReturn(mock(H2InMemoryManager.class));
        given(applicationContextMock.getBean(H2EmbeddedManager.class))
                .willReturn(mock(H2EmbeddedManager.class));
        given(applicationContextMock.getBean(H2ManagedServerManager.class))
                .willReturn(mock(H2ManagedServerManager.class));
        given(applicationContextMock.getBean(H2RemoteServerManager.class))
                .willReturn(mock(H2RemoteServerManager.class));
        given(applicationContextMock.getBean(PostgresqlManager.class))
                .willReturn(mock(PostgresqlManager.class));
    }
}