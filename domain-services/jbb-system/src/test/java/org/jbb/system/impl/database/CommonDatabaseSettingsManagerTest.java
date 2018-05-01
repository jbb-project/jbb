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

import org.jbb.lib.db.DbProperties;
import org.jbb.system.api.database.CommonDatabaseSettings;
import org.jbb.system.api.database.DatabaseSettings;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class CommonDatabaseSettingsManagerTest {

    @Mock
    private DbProperties dbPropertiesMock;

    @InjectMocks
    private CommonDatabaseSettingsManager commonDatabaseSettingsManager;

    @Test
    public void shouldBuildSettings_byTakingProperties() throws Exception {
        // when
        CommonDatabaseSettings currentCommonDatabaseSettings = commonDatabaseSettingsManager
                .getCurrentCommonDatabaseSettings();

        // then
        assertThat(currentCommonDatabaseSettings).isNotNull();
    }

    @Test
    public void shouldSetNewCommonSettings() throws Exception {

        CommonDatabaseSettings commonDatabaseSettings = CommonDatabaseSettings.builder()
                .minimumIdleConnections(2)
                .maximumPoolSize(10)
                .connectionTimeoutMilliseconds(150000)
                .connectionMaxLifetimeMilliseconds(30000)
                .idleTimeoutMilliseconds(1000)
                .validationTimeoutMilliseconds(2000)
                .leakDetectionThresholdMilliseconds(5000)
                .failAtStartingImmediately(true)
                .dropDatabaseAtStart(false)
                .auditEnabled(true)
                .build();

        DatabaseSettings databaseSettings = DatabaseSettings.builder()
                .commonSettings(commonDatabaseSettings)
                .build();

        // when
        commonDatabaseSettingsManager.setCommonSettings(databaseSettings);

        // then
        verify(dbPropertiesMock, times(10)).setProperty(anyString(), anyString());
    }
}