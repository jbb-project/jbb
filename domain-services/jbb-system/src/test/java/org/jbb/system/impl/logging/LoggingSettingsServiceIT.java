/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.logging;

import org.jbb.system.api.logging.LoggingSettingsService;
import org.jbb.system.api.logging.model.LoggingConfiguration;
import org.jbb.system.impl.BaseIT;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;

public class LoggingSettingsServiceIT extends BaseIT {

    @Autowired
    private LoggingSettingsService loggingSettingsService;

    @Test
    public void shouldReturnLoggingConfiguration() {
        // when
        LoggingConfiguration loggingConfiguration = loggingSettingsService.getLoggingConfiguration();

        // then
        assertThat(loggingConfiguration).isNotNull();
    }

    @Test
    public void shouldUpdateDebugModeFlag() {
        // when
        loggingSettingsService.enableDebugLoggingFrameworkMode(false);
        LoggingConfiguration loggingConfiguration = loggingSettingsService.getLoggingConfiguration();

        // then
        assertThat(loggingConfiguration.isDebugLoggingFrameworkMode()).isFalse();
    }

    @Test
    public void shouldUpdatePackagingDataFlag() {
        // when
        loggingSettingsService.showPackagingData(true);
        LoggingConfiguration loggingConfiguration = loggingSettingsService.getLoggingConfiguration();

        // then
        assertThat(loggingConfiguration.isShowPackagingData()).isTrue();
    }

}