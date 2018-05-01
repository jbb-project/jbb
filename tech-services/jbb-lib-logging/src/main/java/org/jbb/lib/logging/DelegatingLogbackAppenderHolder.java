/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.logging;

import ch.qos.logback.ext.spring.DelegatingLogbackAppender;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class DelegatingLogbackAppenderHolder {

    private static DelegatingLogbackAppender appender;

    public static DelegatingLogbackAppender getInstance() {
        if (appender == null) {
            appender = init();
        }
        return appender;
    }

    private static DelegatingLogbackAppender init() {
        DelegatingLogbackAppender appender = new DelegatingLogbackAppender();
        appender.setBeanName(LoggingConfig.PROXY_APPENDER_BEAN_NAME);
        appender.start();
        return appender;
    }

}
