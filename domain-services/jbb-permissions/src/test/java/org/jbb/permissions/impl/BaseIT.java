/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl;

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.eventbus.JbbEventBus;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.system.event.DatabaseSettingsChangedEvent;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {CommonsConfig.class,
    MockCommonsConfig.class, PropertiesConfig.class, DbConfig.class, PermissionsConfig.class,
    EventBusConfig.class, PermissionMockConfig.class})
public abstract class BaseIT {

    public static boolean installed;

    @Autowired
    JbbEventBus eventBus;

    @Before
    public void setUp() throws Exception {
        if (!installed) {
            eventBus.post(new DatabaseSettingsChangedEvent());
            installed = true;
        }
    }

}
