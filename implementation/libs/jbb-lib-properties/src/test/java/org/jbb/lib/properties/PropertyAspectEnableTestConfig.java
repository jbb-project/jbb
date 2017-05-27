/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import org.aeonbits.owner.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
public class PropertyAspectEnableTestConfig {

    @Bean
    public BlankProperties blankProperties(ModulePropertiesFactory modulePropertiesFactory) {
        return modulePropertiesFactory.create(BlankProperties.class);
    }

    @Config.HotReload(type = Config.HotReloadType.ASYNC, value = 1L)
    @Config.Sources({"classpath:jbb-blank.properties"})
    public interface BlankProperties extends ModuleProperties {

        @Key("blank.string")
        String blankString();

        @Key("blank.integer")
        Integer blankInteger();

        @Key("blank.long")
        Long blankLong();

        @Key("blank.boolean")
        Boolean blankBoolean();

        @Key("empty.string")
        String emptyString();

        @Key("empty.integer")
        Integer emptyInteger();

        @Key("empty.long")
        Long emptyLong();

        @Key("empty.boolean")
        Boolean emptyBoolean();
    }

}
