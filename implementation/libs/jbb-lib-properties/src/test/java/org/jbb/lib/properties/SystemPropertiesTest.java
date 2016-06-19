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

import org.apache.commons.lang3.StringUtils;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

public class SystemPropertiesTest {
    private String defaultJbbHomePath = System.getProperty("user.home") + "/jbb";
    private String envJbbHomePath = System.getenv("JBB_HOME");

    @Test
    public void shoutUseEnvVariableValue_whenEnvVariableIsSet() throws Exception {
        // given
        assumeTrue(StringUtils.isNotEmpty(envJbbHomePath));

        // when
        SystemProperties systemProperties = ModulePropertiesFactory.systemProperties();

        // then
        assertThat(systemProperties.jbbDirectory()).isEqualTo(envJbbHomePath);
    }

    @Test
    public void shouldUseDefaultPath_whenEnvVariableIsNotSet() throws Exception {
        // given
        assumeTrue(StringUtils.isEmpty(envJbbHomePath));

        // when
        SystemProperties systemProperties = ModulePropertiesFactory.systemProperties();

        // then
        assertThat(systemProperties.jbbDirectory()).isEqualTo(defaultJbbHomePath);
    }
}