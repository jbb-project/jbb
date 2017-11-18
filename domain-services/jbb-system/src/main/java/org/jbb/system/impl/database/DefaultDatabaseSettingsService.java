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

import java.util.Set;
import javax.annotation.PostConstruct;
import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.Validate;
import org.jbb.lib.db.DbProperties;
import org.jbb.lib.db.DbPropertyChangeListener;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.system.api.database.DatabaseConfigException;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.DatabaseSettingsService;
import org.jbb.system.event.DatabaseSettingsChangedEvent;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DefaultDatabaseSettingsService implements DatabaseSettingsService {

    private final DbProperties dbProperties;
    private final DatabaseSettingsFactory databaseSettingsFactory;
    private final Validator validator;
    private final ReconnectionToDbPropertyListener reconnectionPropertyListener;
    private final DbPropertyChangeListener dbPropertyChangeListener;
    private final DatabaseSettingsManager settingsManager;
    private final DatabaseSettingsSaver settingsSaver;
    private final JbbEventBus eventBus;

    @PostConstruct
    public void addReconnectionPropertyListenerToDbProperties() {
        dbProperties.addPropertyChangeListener(reconnectionPropertyListener);
    }

    @Override
    public DatabaseSettings getDatabaseSettings() {
        return databaseSettingsFactory.currentDatabaseSettings();
    }

    @Override
    public void setDatabaseSettings(DatabaseSettings newDatabaseSettings) {
        Validate.notNull(newDatabaseSettings);

        Set<ConstraintViolation<DatabaseSettings>> validationResult = validator.validate(newDatabaseSettings);
        if (!validationResult.isEmpty()) {
            throw new DatabaseConfigException(validationResult);
        }

        dbProperties.removePropertyChangeListener(dbPropertyChangeListener);
        dbProperties.removePropertyChangeListener(reconnectionPropertyListener);
        try {
            settingsSaver.setDatabaseSettings(newDatabaseSettings);
        } finally {
            dbProperties.addPropertyChangeListener(dbPropertyChangeListener);
            addReconnectionPropertyListenerToDbProperties();
        }
        settingsManager.triggerRefresh();
        eventBus.post(new DatabaseSettingsChangedEvent());
    }
}
