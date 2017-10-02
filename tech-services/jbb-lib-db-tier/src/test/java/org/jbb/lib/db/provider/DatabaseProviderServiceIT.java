/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db.provider;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.lib.cache.CacheConfig;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.db.DbProperties;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, PropertiesConfig.class, DbConfig.class,
    CacheConfig.class,
    MockCommonsConfig.class})
public class DatabaseProviderServiceIT {

    @Autowired
    private DatabaseProviderService databaseProviderService;

    @Autowired
    private DbProperties dbProperties;

    @Test
    public void shouldSwitchBetweenDatabaseProviders() throws Exception {
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