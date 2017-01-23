/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.service;

import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LoggingConfiguration;

import java.util.Optional;

public interface LoggingSettingsService {
    LoggingConfiguration getLoggingConfiguration();

    void addAppender(LogAppender appender);

    void updateAppender(LogAppender appender);

    void deleteAppender(LogAppender appender);

    void addLogger(AppLogger logger);

    void updateLogger(AppLogger logger);

    void deleteLogger(AppLogger logger);

    void enableDebugLoggingFrameworkMode(boolean enable);

    void showPackagingData(boolean showPackagingData);

    Optional<LogAppender> getAppender(String appenderName);
}
