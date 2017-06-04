/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.base.data;

import org.jbb.system.web.base.form.DatabaseSettingsForm;
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
        given(databaseSettingsForm.getConnectionTimeOutMilliseconds()).willReturn(10000);
        given(databaseSettingsForm.isFailAtStartingImmediately()).willReturn(false);
        given(databaseSettingsForm.isDropDatabaseAtStart()).willReturn(true);
        given(databaseSettingsForm.isAuditEnabled()).willReturn(false);

        // when
        FormDatabaseSettings formDatabaseSettings = new FormDatabaseSettings(databaseSettingsForm);

        // then
        assertThat(formDatabaseSettings.databaseFileName()).isEqualTo("jbb.db");
        assertThat(formDatabaseSettings.minimumIdleConnections()).isEqualTo(10);
        assertThat(formDatabaseSettings.maximumPoolSize()).isEqualTo(20);
        assertThat(formDatabaseSettings.connectionTimeoutMilliseconds()).isEqualTo(10000);
        assertThat(formDatabaseSettings.failAtStartingImmediately()).isFalse();
        assertThat(formDatabaseSettings.dropDatabaseAtStart()).isTrue();
        assertThat(formDatabaseSettings.auditEnabled()).isFalse();
    }
}