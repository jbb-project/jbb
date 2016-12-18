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
import org.jbb.lib.db.DbProperties;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.system.api.exception.DatabaseConfigException;
import org.jbb.system.api.model.DatabaseSettings;
import org.jbb.system.api.service.DatabaseSettingsService;
import org.jbb.system.event.ConnectionToDatabaseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@Service
public class DatabaseSettingsServiceImpl implements DatabaseSettingsService {
    private final DbProperties dbProperties;
    private final DatabaseSettingsImplFactory databaseSettingsFactory;
    private final Validator validator;
    private final JbbEventBus eventBus;

    @Autowired
    public DatabaseSettingsServiceImpl(DbProperties dbProperties,
                                       DatabaseSettingsImplFactory databaseSettingsFactory,
                                       Validator validator, JbbEventBus eventBus) {
        this.dbProperties = dbProperties;
        this.databaseSettingsFactory = databaseSettingsFactory;
        this.validator = validator;
        this.eventBus = eventBus;
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

        dbProperties.setProperty(DbProperties.DB_FILENAME_KEY, newDatabaseSettings.databaseFileName());
        dbProperties.setProperty(DbProperties.DB_MIN_IDLE_KEY, Integer.toString(newDatabaseSettings.minimumIdleConnections()));
        dbProperties.setProperty(DbProperties.DB_MAX_POOL_KEY, Integer.toString(newDatabaseSettings.maximumPoolSize()));
        dbProperties.setProperty(DbProperties.DB_CONN_TIMEOUT_MS_KEY, Integer.toString(newDatabaseSettings.connectionTimeoutMilliseconds()));
        dbProperties.setProperty(DbProperties.DB_INIT_FAIL_FAST_KEY, Boolean.toString(newDatabaseSettings.failAtStartingImmediately()));
        dbProperties.setProperty(DbProperties.DB_DROP_DURING_START_KEY, Boolean.toString(newDatabaseSettings.dropDatabaseAtStart()));

        eventBus.post(new ConnectionToDatabaseEvent());
    }
}
