/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties.encrypt;

import org.aeonbits.owner.Config;
import org.jbb.lib.properties.ModuleProperties;
import org.jbb.lib.properties.ModulePropertiesFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class PropertyEncryptionEnableTestConfig {

    @Bean
    public ExampleProperties exampleProperties(ModulePropertiesFactory modulePropertiesFactory) {
        return modulePropertiesFactory.create(ExampleProperties.class);
    }

    @Config.HotReload(type = Config.HotReloadType.ASYNC, value = 1L)
    @Config.Sources({"file:${jbb.home}/jbb-testbed.properties"})
    interface ExampleProperties extends ModuleProperties {

        @Key("foo")
        String foo();

        @Key("bar")
        Integer bar();
    }

}
