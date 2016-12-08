/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.service;

import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.Logger;
import org.jbb.system.api.model.logging.LoggingConfiguration;

public interface LoggingSettingsService {
    LoggingConfiguration getLoggingConfiguration();

    void addAppender(LogAppender appender);

    void updateAppender(LogAppender appender);

    void deleteAppender(LogAppender appender);

    void addLogger(Logger logger);

    void updateLogger(Logger logger);

    void deleteLogger(Logger logger);

    void enableDebugLoggingFrameworkMode(boolean enable);

    void showPackagingData(boolean showPackagingData);
}
