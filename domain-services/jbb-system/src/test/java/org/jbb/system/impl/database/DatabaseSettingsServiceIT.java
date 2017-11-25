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

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import org.jbb.lib.cache.CacheConfig;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.logging.LoggingConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.system.api.database.DatabaseConfigException;
import org.jbb.system.api.database.DatabaseProvider;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.DatabaseSettingsService;
import org.jbb.system.impl.SystemConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, SystemConfig.class, MvcConfig.class,
    LoggingConfig.class,
    EventBusConfig.class, PropertiesConfig.class, DbConfig.class, CacheConfig.class,
    MockCommonsConfig.class})
public class DatabaseSettingsServiceIT {

    @Autowired
    private DatabaseSettingsService databaseSettingsService;


    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullNewDatabaseSettingsPassed() throws Exception {
        // when
        databaseSettingsService.setDatabaseSettings(null);
    }


    @Test
    public void shouldGetDatabaseSettings() throws Exception {
        // when
        DatabaseSettings databaseSettings = databaseSettingsService.getDatabaseSettings();

        // then
        assertThat(databaseSettings).isNotNull();
    }

    @Test(expected = DatabaseConfigException.class)
    public void shouldThrowDatabaseConfigException_whenValidationFailed() throws Exception {
        // given
        DatabaseSettings databaseSettings = databaseSettingsService.getDatabaseSettings();
        databaseSettings.setCurrentDatabaseProvider(null);

        // when
        databaseSettingsService.setDatabaseSettings(databaseSettings);

        // then
        // throw DatabaseConfigException
    }

    @Test
    public void shouldSetDatabaseSettings() throws Exception {
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