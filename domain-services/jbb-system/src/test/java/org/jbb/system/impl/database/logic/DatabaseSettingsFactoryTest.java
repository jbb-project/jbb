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

import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.CommonDatabaseSettings;
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
public class DatabaseSettingsFactoryTest {
    @Mock
    private DbProperties dbPropertiesMock;

    @InjectMocks
    private DatabaseSettingsFactory databaseSettingsFactory;

    @Test
    public void shouldReturnCurrentSettings() throws Exception {
        // given
        given(dbPropertiesMock.minimumIdle()).willReturn(5);
        given(dbPropertiesMock.maxPool()).willReturn(10);
        given(dbPropertiesMock.connectionTimeoutMilliseconds()).willReturn(1000);
        given(dbPropertiesMock.connectionMaxLifetimeMilliseconds()).willReturn(500000);
        given(dbPropertiesMock.idleTimeoutMilliseconds()).willReturn(20000);
        given(dbPropertiesMock.validationTimeoutMilliseconds()).willReturn(5000);
        given(dbPropertiesMock.leakDetectionThresholdMilliseconds()).willReturn(5);
        given(dbPropertiesMock.failFastDuringInit()).willReturn(true);
        given(dbPropertiesMock.dropDbDuringStart()).willReturn(false);
        given(dbPropertiesMock.auditEnabled()).willReturn(true);

        given(dbPropertiesMock.currentProvider()).willReturn("h2-managed-server");

        given(dbPropertiesMock.h2ManagedServerDbName()).willReturn("jbb.db");
        given(dbPropertiesMock.h2ManagedServerDbPort()).willReturn(9000);
        given(dbPropertiesMock.h2ManagedServerUsername()).willReturn("jbb");
        given(dbPropertiesMock.h2ManagedServerPassword()).willReturn("pass1");
        given(dbPropertiesMock.h2ManagedServerFilePassword()).willReturn("pass2");
        given(dbPropertiesMock.h2ManagedServerConnectionType()).willReturn("tcp");
        given(dbPropertiesMock.h2ManagedServerDbEncryptionAlgorithm()).willReturn("aes");

        // when
        DatabaseSettings databaseSettings = databaseSettingsFactory.currentDatabaseSettings();

        // then
        CommonDatabaseSettings commonSettings = databaseSettings.getCommonSettings();
        assertThat(commonSettings.getMinimumIdleConnections()).isEqualTo(5);
        assertThat(commonSettings.getMaximumPoolSize()).isEqualTo(10);
        assertThat(commonSettings.getConnectionTimeoutMilliseconds()).isEqualTo(1000);
        assertThat(commonSettings.getConnectionMaxLifetimeMilliseconds()).isEqualTo(500000);
        assertThat(commonSettings.getIdleTimeoutMilliseconds()).isEqualTo(20000);
        assertThat(commonSettings.getValidationTimeoutMilliseconds()).isEqualTo(5000);
        assertThat(commonSettings.getLeakDetectionThresholdMilliseconds()).isEqualTo(5);
        assertThat(commonSettings.isFailAtStartingImmediately()).isTrue();
        assertThat(commonSettings.isDropDatabaseAtStart()).isFalse();
        assertThat(commonSettings.isAuditEnabled()).isTrue();

        H2ManagedServerSettings h2ManagedServerSettings = (H2ManagedServerSettings) databaseSettings
            .getProviderSettings();
        assertThat(h2ManagedServerSettings.getDatabaseFileName()).isEqualTo("jbb.db");
        assertThat(h2ManagedServerSettings.getPort()).isEqualTo(9000);
        assertThat(h2ManagedServerSettings.getUsername()).isEqualTo("jbb");
        assertThat(h2ManagedServerSettings.getUsernamePassword()).isEqualTo("pass1");
        assertThat(h2ManagedServerSettings.getFilePassword()).isEqualTo("pass2");
        assertThat(h2ManagedServerSettings.getConnectionType()).isEqualTo(H2ConnectionType.TCP);
        assertThat(h2ManagedServerSettings.getEncryptionAlgorithm())
            .isEqualTo(H2EncryptionAlgorithm.AES);

    }
}