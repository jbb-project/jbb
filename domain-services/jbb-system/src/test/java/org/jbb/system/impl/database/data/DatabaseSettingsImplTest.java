/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.data;


import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class DatabaseSettingsImplTest {
    @Test
    public void pojoTest() throws Exception {
        DatabaseSettingsImpl databaseSettings = new DatabaseSettingsImpl();

        databaseSettings.setDatabaseFileName("jbb.db");
        assertThat(databaseSettings.getDatabaseFileName()).isEqualTo("jbb.db");

        databaseSettings.setMinimumIdleConnections(123);
        assertThat(databaseSettings.getMinimumIdleConnections()).isEqualTo(123);

        databaseSettings.setMaximumPoolSize(6000);
        assertThat(databaseSettings.getMaximumPoolSize()).isEqualTo(6000);

        databaseSettings.setConnectionTimeoutMilliseconds(10000);
        assertThat(databaseSettings.getConnectionTimeoutMilliseconds()).isEqualTo(10000);

        databaseSettings.setConnectionMaxLifetimeMilliseconds(500000);
        assertThat(databaseSettings.getConnectionMaxLifetimeMilliseconds()).isEqualTo(500000);

        databaseSettings.setIdleTimeoutMilliseconds(20000);
        assertThat(databaseSettings.getIdleTimeoutMilliseconds()).isEqualTo(20000);

        databaseSettings.setValidationTimeoutMilliseconds(5000);
        assertThat(databaseSettings.getValidationTimeoutMilliseconds()).isEqualTo(5000);

        databaseSettings.setLeakDetectionThresholdMilliseconds(5);
        assertThat(databaseSettings.getLeakDetectionThresholdMilliseconds()).isEqualTo(5);

        databaseSettings.setFailAtStartingImmediately(true);
        assertThat(databaseSettings.isFailAtStartingImmediately()).isTrue();

        databaseSettings.setDropDatabaseAtStart(false);
        assertThat(databaseSettings.isDropDatabaseAtStart()).isFalse();

        databaseSettings.setAuditEnabled(false);
        assertThat(databaseSettings.isAuditEnabled()).isFalse();
    }
}