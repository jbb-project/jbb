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

import org.jbb.lib.commons.CommonsConfig;
import org.jbb.lib.test.MockCommonsConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;
import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {PropertiesConfig.class, MockCommonsConfig.class, CommonsConfig.class})
public class ModulePropertiesFactoryIT {
    @Autowired
    private ModulePropertiesFactory propertiesFactory;

    @Test
    public void shouldUseValuesFromFileOnClasspath() throws Exception {
        // when
        ExampleProperties exampleConfig = propertiesFactory.create(ExampleProperties.class);

        // then
        assertThat(exampleConfig.foo()).isEqualTo("value1");
        assertThat(exampleConfig.bar()).isEqualTo("value2");
    }

    @Test
    public void shouldNotRegisterListeners_whenPropertiesAreStatic() throws Exception {
        // when
        ExampleStaticProperties exampleStaticProperties = propertiesFactory.create(ExampleStaticProperties.class);

        // then
        // (only ModuleProperties type can bind some PropertyListeners)
        assertThat(exampleStaticProperties).isNotInstanceOf(ModuleProperties.class);
    }

    @Test
    public void shouldUpdateProperty_whenSetterInvoked() throws Exception {
        // given
        ExampleProperties exampleConfig = propertiesFactory.create(ExampleProperties.class);

        // when
        exampleConfig.setProperty("foo", "newFooValue");

        // then
        assertThat(exampleConfig.foo()).isEqualTo("newFooValue");
    }


    @LoadPolicy(LoadType.MERGE)
    @Sources({"classpath:test.properties"})
    private interface ExampleProperties extends ModuleProperties {

        String foo();

        String bar();
    }

    @LoadPolicy(LoadType.MERGE)
    @Sources({"classpath:test.static.properties"})
    private interface ExampleStaticProperties extends ModuleStaticProperties {

        String foo();

        String bar();
    }
}
