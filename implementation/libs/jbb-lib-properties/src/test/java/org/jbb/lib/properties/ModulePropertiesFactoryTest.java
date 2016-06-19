/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.Modifier;

import static org.aeonbits.owner.Config.LoadPolicy;
import static org.aeonbits.owner.Config.LoadType;
import static org.aeonbits.owner.Config.Sources;
import static org.assertj.core.api.Assertions.assertThat;

public class ModulePropertiesFactoryTest {

    @Test
    public void shouldCannotBePossibleToCreateObject() throws Exception {
        Constructor<?>[] c = ModulePropertiesFactory.class.getDeclaredConstructors();
        for (Constructor<?> constructor : c) {
            assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();
        }
    }

    @Test
    public void shouldUseValuesFromFileOnClasspath() throws Exception {
        // when
        ExampleProperties exampleConfig = ModulePropertiesFactory.create(ExampleProperties.class);

        // then
        assertThat(exampleConfig.foo()).isEqualTo("value1");
        assertThat(exampleConfig.bar()).isEqualTo("value2");
    }

    @LoadPolicy(LoadType.MERGE)
    @Sources({"classpath:test.properties"})
    private interface ExampleProperties extends ModuleProperties {

        String foo();

        String bar();
    }
}
