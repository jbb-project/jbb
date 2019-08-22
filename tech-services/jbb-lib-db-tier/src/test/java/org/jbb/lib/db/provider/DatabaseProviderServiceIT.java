/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db.provider;

import org.jbb.lib.db.DbConfig;
import org.jbb.lib.db.DbProperties;
import org.jbb.lib.test.MockCommonsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {DbConfig.class, MockCommonsConfig.class})
public class DatabaseProviderServiceIT {

    @Autowired
    private DatabaseProviderService databaseProviderService;

    @Autowired
    private DbProperties dbProperties;

    @Test
    public void shouldSwitchBetweenDatabaseProviders() {
        assertThat(databaseProviderService.getCurrentProvider())
                .isInstanceOf(H2InMemoryProvider.class);
        dbProperties.setProperty(DbProperties.DB_CURRENT_PROVIDER, "h2-managed-server");
        assertThat(databaseProviderService.getCurrentProvider())
                .isInstanceOf(H2ManagedServerProvider.class);
        dbProperties.setProperty(DbProperties.DB_CURRENT_PROVIDER, "h2-embedded");
        assertThat(databaseProviderService.getCurrentProvider())
                .isInstanceOf(H2EmbeddedProvider.class);
    }
}