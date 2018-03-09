/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database;

import org.jbb.system.api.database.DatabaseConfigException;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.DatabaseSettingsService;
import org.jbb.system.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class DatabaseSettingsServiceIT extends BaseIT {

    @Autowired
    private DatabaseSettingsService databaseSettingsService;


    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullNewDatabaseSettingsPassed() {
        // when
        databaseSettingsService.setDatabaseSettings(null);
    }


    @Test
    public void shouldGetDatabaseSettings() {
        // when
        DatabaseSettings databaseSettings = databaseSettingsService.getDatabaseSettings();

        // then
        assertThat(databaseSettings).isNotNull();
    }

    @Test(expected = DatabaseConfigException.class)
    public void shouldThrowDatabaseConfigException_whenValidationFailed() {
        // given
        DatabaseSettings databaseSettings = databaseSettingsService.getDatabaseSettings();
        databaseSettings.setCurrentDatabaseProvider(null);

        // when
        databaseSettingsService.setDatabaseSettings(databaseSettings);

        // then
        // throw DatabaseConfigException
    }

    @Test
    public void shouldSetDatabaseSettings() {
        // given
        DatabaseSettings databaseSettings = databaseSettingsService.getDatabaseSettings();
        databaseSettings.setCurrentDatabaseProvider(DatabaseProvider.H2_IN_MEMORY);

        // when
        databaseSettingsService.setDatabaseSettings(databaseSettings);
        DatabaseSettings newDatabaseSettings = databaseSettingsService.getDatabaseSettings();

        // then
        assertThat(newDatabaseSettings.getCurrentDatabaseProvider())
                .isEqualTo(DatabaseProvider.H2_IN_MEMORY);
    }
}