/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.database.logic;

import org.apache.commons.lang.Validate;
import org.jbb.lib.db.DbStaticProperties;
import org.jbb.system.api.model.DatabaseSettings;
import org.jbb.system.api.service.DatabaseSettingsService;
import org.jbb.system.impl.database.data.DatabaseSettingsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DatabaseSettingsServiceImpl implements DatabaseSettingsService {
    private final DbStaticProperties dbProperties;
    private boolean restartNeeded = false;

    @Autowired
    public DatabaseSettingsServiceImpl(DbStaticProperties dbProperties) {
        this.dbProperties = dbProperties;
    }

    @Override
    public boolean restartNeeded() {
        return restartNeeded;
    }

    @Override
    public DatabaseSettings getDatabaseSettings() {
        DatabaseSettingsImpl databaseSettings = new DatabaseSettingsImpl();
        databaseSettings.setDatabaseFileName(dbProperties.dbFilename());
        databaseSettings.setMinimumIdleConnections(dbProperties.minimumIdle());
        databaseSettings.setMaximumPoolSize(dbProperties.maxPool());
        databaseSettings.setConnectionTImeOutMilliseconds(dbProperties.connectionTimeoutMiliseconds());
        databaseSettings.setFailAtStartingImmediately(dbProperties.failFastDuringInit());
        databaseSettings.setDropDatabaseAtStart(dbProperties.dropDbDuringStart());
        return databaseSettings;
    }

    @Override
    public void setDatabaseSettings(DatabaseSettings databaseSettings) {
        Validate.notNull(databaseSettings);
        dbProperties.setProperty(DbStaticProperties.DB_FILENAME_KEY, databaseSettings.databaseFileName());
        dbProperties.setProperty(DbStaticProperties.DB_MIN_IDLE_KEY, Integer.toString(databaseSettings.minimumIdleConnections()));
        dbProperties.setProperty(DbStaticProperties.DB_MAX_POOL_KEY, Integer.toString(databaseSettings.maximumPoolSize()));
        dbProperties.setProperty(DbStaticProperties.DB_CONN_TIMEOUT_MS_KEY, Integer.toString(databaseSettings.connectionTimeoutMilliseconds()));
        dbProperties.setProperty(DbStaticProperties.DB_INIT_FAIL_FAST_KEY, Boolean.toString(databaseSettings.failAtStartingImmediately()));
        dbProperties.setProperty(DbStaticProperties.DB_DROP_DURING_START_KEY, Boolean.toString(databaseSettings.dropDatabaseAtStart()));
        restartNeeded = true;
    }
}
