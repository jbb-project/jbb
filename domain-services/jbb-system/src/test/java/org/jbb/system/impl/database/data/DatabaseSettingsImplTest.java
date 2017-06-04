/*
 * Copyright (C) 2016 the original author or authors.
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
        assertThat(databaseSettings.databaseFileName()).isEqualTo("jbb.db");

        databaseSettings.setMinimumIdleConnections(123);
        assertThat(databaseSettings.minimumIdleConnections()).isEqualTo(123);

        databaseSettings.setMaximumPoolSize(6000);
        assertThat(databaseSettings.maximumPoolSize()).isEqualTo(6000);

        databaseSettings.setConnectionTimeOutMilliseconds(10000);
        assertThat(databaseSettings.connectionTimeoutMilliseconds()).isEqualTo(10000);

        databaseSettings.setFailAtStartingImmediately(true);
        assertThat(databaseSettings.failAtStartingImmediately()).isTrue();

        databaseSettings.setDropDatabaseAtStart(false);
        assertThat(databaseSettings.dropDatabaseAtStart()).isFalse();

        databaseSettings.setAuditEnabled(false);
        assertThat(databaseSettings.auditEnabled()).isFalse();
    }
}