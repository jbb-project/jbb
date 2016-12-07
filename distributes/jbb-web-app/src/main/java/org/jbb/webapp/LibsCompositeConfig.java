/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.jbb.lib.db.DbConfig;
import org.jbb.lib.eventbus.EventBusConfig;
import org.jbb.lib.logging.LoggingConfig;
import org.jbb.lib.mvc.MvcConfig;
import org.jbb.lib.properties.PropertiesConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({PropertiesConfig.class, MvcConfig.class, EventBusConfig.class,
        DbConfig.class, LoggingConfig.class})
class LibsCompositeConfig {
}
