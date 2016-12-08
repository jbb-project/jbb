/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.logging.logic;

import org.jbb.lib.logging.jaxb.Appender;
import org.jbb.lib.logging.jaxb.Configuration;
import org.jbb.lib.logging.jaxb.Logger;
import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.springframework.stereotype.Component;

@Component
public class LoggingConfigMapper {
    public LoggingConfiguration buildConfiguration(Configuration jaxbConfiguration) {
        // todo
        return null;
    }

    public Appender buildJaxb(LogAppender appender) {
        // todo
        return null;
    }

    public Logger buildJaxb(AppLogger logger) {
        // todo
        return null;
    }
}
