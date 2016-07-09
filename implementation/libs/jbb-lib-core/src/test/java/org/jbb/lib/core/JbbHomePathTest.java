/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.core;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;

public class JbbHomePathTest {
    private String defaultJbbHomePath = System.getProperty("user.home") + "/jbb";
    private String envJbbHomePath = System.getenv("JBB_HOME");

    private JbbHomePath jbbHomePath;

    @Before
    public void setUp() throws Exception {
        jbbHomePath = new JbbHomePath();
        jbbHomePath.resolveEffectiveAndStoreToSystemProperty();
    }

    @Test
    public void shouldUseEnvVariableValue_whenEnvVariableIsSet() throws Exception {
        // given
        assumeTrue(StringUtils.isNotEmpty(envJbbHomePath));

        // when
        String effectiveJbbHomePath = jbbHomePath.getEffective();

        // then
        assertThat(effectiveJbbHomePath).isEqualTo(envJbbHomePath);
    }

    @Test
    public void shouldUseDefaultPath_whenEnvVariableIsNotSet() throws Exception {
        // given
        assumeTrue(StringUtils.isEmpty(envJbbHomePath));

        // when
        String effectiveJbbHomePath = jbbHomePath.getEffective();

        // then
        assertThat(effectiveJbbHomePath).isEqualTo(defaultJbbHomePath);
    }
}