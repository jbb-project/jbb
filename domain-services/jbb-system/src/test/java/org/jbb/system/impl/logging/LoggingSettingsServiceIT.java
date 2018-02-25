/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.logging;

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.logging.LoggingConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.jbb.system.api.logging.LoggingSettingsService;
import org.jbb.system.api.logging.model.LoggingConfiguration;
import org.jbb.system.impl.SystemConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = {CommonsConfig.class, SystemConfig.class, MvcConfig.class, LoggingConfig.class, EventBusConfig.class, PropertiesConfig.class, DbConfig.class, MockCommonsConfig.class})
public class LoggingSettingsServiceIT {

    @Autowired
    private LoggingSettingsService loggingSettingsService;

    @Test
    public void shouldReturnLoggingConfiguration() throws Exception {
        // when
        LoggingConfiguration loggingConfiguration = loggingSettingsService.getLoggingConfiguration();

        // then
        assertThat(loggingConfiguration).isNotNull();
    }

    @Test
    public void shouldUpdateDebugModeFlag() throws Exception {
        // when
        loggingSettingsService.enableDebugLoggingFrameworkMode(false);
        LoggingConfiguration loggingConfiguration = loggingSettingsService.getLoggingConfiguration();

        // then
        assertThat(loggingConfiguration.isDebugLoggingFrameworkMode()).isFalse();
    }

    @Test
    public void shouldUpdatePackagingDataFlag() throws Exception {
        // when
        loggingSettingsService.showPackagingData(true);
        LoggingConfiguration loggingConfiguration = loggingSettingsService.getLoggingConfiguration();

        // then
        assertThat(loggingConfiguration.isShowPackagingData()).isTrue();
    }

}