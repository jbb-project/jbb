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

import org.apache.commons.lang3.Validate;
import org.jbb.lib.db.DbStaticProperties;
import org.jbb.system.api.exception.DatabaseConfigException;
import org.jbb.system.api.model.DatabaseSettings;
import org.jbb.system.api.service.DatabaseSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Service
public class DatabaseSettingsServiceImpl implements DatabaseSettingsService {
    private final DbStaticProperties dbProperties;
    private final DatabaseSettingsImplFactory databaseSettingsFactory;
    private final Validator validator;

    private boolean restartNeeded = false;

    @Autowired
    public DatabaseSettingsServiceImpl(DbStaticProperties dbProperties,
                                       DatabaseSettingsImplFactory databaseSettingsFactory,
                                       Validator validator) {
        this.dbProperties = dbProperties;
        this.databaseSettingsFactory = databaseSettingsFactory;
        this.validator = validator;
    }

    @Override
    public boolean restartNeeded() {
        return restartNeeded;
    }

    @Override
    public DatabaseSettings getDatabaseSettings() {
        return databaseSettingsFactory.currentDatabaseSettings();
    }

    @Override
    public void setDatabaseSettings(DatabaseSettings databaseSettings) {
        Validate.notNull(databaseSettings);

        DatabaseSettings newDatabaseSettings = databaseSettingsFactory.create(databaseSettings);
        Set<ConstraintViolation<DatabaseSettings>> validationResult = validator.validate(newDatabaseSettings);
        if (!validationResult.isEmpty()) {
            throw new DatabaseConfigException(validationResult);
        }

        dbProperties.setProperty(DbStaticProperties.DB_FILENAME_KEY, newDatabaseSettings.databaseFileName());
        dbProperties.setProperty(DbStaticProperties.DB_MIN_IDLE_KEY, Integer.toString(newDatabaseSettings.minimumIdleConnections()));
        dbProperties.setProperty(DbStaticProperties.DB_MAX_POOL_KEY, Integer.toString(newDatabaseSettings.maximumPoolSize()));
        dbProperties.setProperty(DbStaticProperties.DB_CONN_TIMEOUT_MS_KEY, Integer.toString(newDatabaseSettings.connectionTimeoutMilliseconds()));
        dbProperties.setProperty(DbStaticProperties.DB_INIT_FAIL_FAST_KEY, Boolean.toString(newDatabaseSettings.failAtStartingImmediately()));
        dbProperties.setProperty(DbStaticProperties.DB_DROP_DURING_START_KEY, Boolean.toString(newDatabaseSettings.dropDatabaseAtStart()));

        restartNeeded = true;
    }
}