/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend;

import org.jbb.frontend.properties.FrontendProperties;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan("org.jbb.frontend")
public class FrontendConfig {

    @Autowired
    private ModulePropertiesFactory propertiesFactory;

    @Bean
    public FrontendProperties frontendProperties() {
        return propertiesFactory.create(FrontendProperties.class);
    }
}
