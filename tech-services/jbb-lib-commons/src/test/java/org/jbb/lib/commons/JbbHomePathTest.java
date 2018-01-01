/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons;

import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;

@RunWith(MockitoJUnitRunner.class)
public class JbbHomePathTest {
    @Rule
    public TemporaryFolder temp = new TemporaryFolder();
    @Mock
    JndiValueReader jndiValueReaderMock;
    private String defaultJbbHomePath = System.getProperty("user.home") + "/jbb";
    private String envJbbHomePath = System.getenv("JBB_HOME");
    private JbbHomePath jbbHomePath;

    @Before
    public void setUp() throws Exception {
        jbbHomePath = new JbbHomePath(jndiValueReaderMock);
        jbbHomePath.resolveEffective();
    }

    @Test
    public void shouldUseEnvVariableValue_whenEnvVariableIsSet() {
        // given
        assumeTrue(StringUtils.isNotEmpty(envJbbHomePath));

        // when
        String effectiveJbbHomePath = jbbHomePath.getEffective();

        // then
        assertThat(effectiveJbbHomePath).isEqualTo(envJbbHomePath);
    }

    @Test
    public void shouldUseDefaultPath_whenEnvVariableIsNotSet() {
        // given
        assumeTrue(StringUtils.isEmpty(envJbbHomePath));

        // when
        String effectiveJbbHomePath = jbbHomePath.getEffective();

        // then
        assertThat(effectiveJbbHomePath).isEqualTo(defaultJbbHomePath);
    }

    @Test
    public void shouldUseValueFromJndi_ifPresent() throws Exception {
        // given
        File tempFolder = temp.newFolder();

        BDDMockito.given(jndiValueReaderMock.readValue(any())).willReturn(tempFolder.getAbsolutePath());
        jbbHomePath = new JbbHomePath(jndiValueReaderMock);
        jbbHomePath.setUp();

        // when
        String effectiveJbbHomePath = jbbHomePath.getEffective();

        // then
        assertThat(effectiveJbbHomePath).isEqualTo(tempFolder.getAbsolutePath());
    }
}