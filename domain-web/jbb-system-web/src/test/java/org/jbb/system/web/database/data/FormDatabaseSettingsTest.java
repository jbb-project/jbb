/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.database.data;

import org.jbb.system.web.database.form.DatabaseSettingsForm;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class FormDatabaseSettingsTest {
    @Test
    public void buildTest() throws Exception {
        // given
        DatabaseSettingsForm databaseSettingsForm = mock(DatabaseSettingsForm.class);
        given(databaseSettingsForm.getDatabaseFileName()).willReturn("jbb.db");
        given(databaseSettingsForm.getMinimumIdleConnections()).willReturn(10);
        given(databaseSettingsForm.getMaximumPoolSize()).willReturn(20);
        given(databaseSettingsForm.getConnectionTimeoutMilliseconds()).willReturn(10000);
        given(databaseSettingsForm.getConnectionMaxLifetimeMilliseconds()).willReturn(500000);
        given(databaseSettingsForm.getIdleTimeoutMilliseconds()).willReturn(20000);
        given(databaseSettingsForm.getValidationTimeoutMilliseconds()).willReturn(5000);
        given(databaseSettingsForm.getLeakDetectionThresholdMilliseconds()).willReturn(10);
        given(databaseSettingsForm.isFailAtStartingImmediately()).willReturn(false);
        given(databaseSettingsForm.isDropDatabaseAtStart()).willReturn(true);
        given(databaseSettingsForm.isAuditEnabled()).willReturn(false);

        // when
        FormDatabaseSettings formDatabaseSettings = new FormDatabaseSettings(databaseSettingsForm);

        // then
        assertThat(formDatabaseSettings.getDatabaseFileName()).isEqualTo("jbb.db");
        assertThat(formDatabaseSettings.getMinimumIdleConnections()).isEqualTo(10);
        assertThat(formDatabaseSettings.getMaximumPoolSize()).isEqualTo(20);
        assertThat(formDatabaseSettings.getConnectionTimeoutMilliseconds()).isEqualTo(10000);
        assertThat(formDatabaseSettings.getConnectionMaxLifetimeMilliseconds()).isEqualTo(500000);
        assertThat(formDatabaseSettings.getIdleTimeoutMilliseconds()).isEqualTo(20000);
        assertThat(formDatabaseSettings.getValidationTimeoutMilliseconds()).isEqualTo(5000);
        assertThat(formDatabaseSettings.getLeakDetectionThresholdMilliseconds()).isEqualTo(10);
        assertThat(formDatabaseSettings.isFailAtStartingImmediately()).isFalse();
        assertThat(formDatabaseSettings.isDropDatabaseAtStart()).isTrue();
        assertThat(formDatabaseSettings.isAuditEnabled()).isFalse();
    }
}