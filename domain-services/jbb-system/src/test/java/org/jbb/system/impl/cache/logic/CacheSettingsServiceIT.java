/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.cache.logic;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.lib.cache.CacheConfig;
import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.logging.LoggingConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.system.api.cache.CacheSettings;
import org.jbb.system.api.cache.CacheSettingsService;
import org.jbb.system.impl.SystemConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, SystemConfig.class, MvcConfig.class, LoggingConfig.class,
        EventBusConfig.class, PropertiesConfig.class, DbConfig.class, CacheConfig.class, MockCommonsConfig.class})
public class CacheSettingsServiceIT {

    @Autowired
    private CacheSettingsService cacheSettingsService;

    @Test(expected = NullPointerException.class)
    public void shouldThrowNPE_whenNullNewCacheSettingsPassed() throws Exception {
        // when
        cacheSettingsService.setCacheSettings(null);
    }

    @Test
    public void defaultCacheSettings_shouldEnableApplicationAndSecondLevelCaching() throws Exception {
        // when
        CacheSettings cacheSettings = cacheSettingsService.getCacheSettings();

        // then
        assertThat(cacheSettings.isApplicationCacheEnabled()).isTrue();
        assertThat(cacheSettings.isSecondLevelCacheEnabled()).isTrue();
        assertThat(cacheSettings.isQueryCacheEnabled()).isFalse();
    }

    @Test
    public void shouldSetNewCacheSettings_whenProvided() throws Exception {
        // given
        CacheSettings newCacheSettings = CacheSettings.builder()
                .applicationCacheEnabled(false)
                .secondLevelCacheEnabled(true)
                .queryCacheEnabled(true)
                .build();

        // when
        cacheSettingsService.setCacheSettings(newCacheSettings);
        CacheSettings result = cacheSettingsService.getCacheSettings();

        // then
        assertThat(result.isApplicationCacheEnabled()).isFalse();
        assertThat(result.isSecondLevelCacheEnabled()).isTrue();
        assertThat(result.isQueryCacheEnabled()).isTrue();

    }
}