/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.health.checks;

import com.codahale.metrics.health.HealthCheck;

import org.jbb.lib.commons.JbbMetaData;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class JbbDirectoryHealthCheckTest {

    @Rule
    public TemporaryFolder temp = new TemporaryFolder();

    @Mock
    private JbbMetaData jbbMetaDataMock;

    @InjectMocks
    private JbbDirectoryHealthCheck jbbDirectoryHealthCheck;

    @Test
    public void shouldReturnNotEmptyName() {
        // when
        String checkName = jbbDirectoryHealthCheck.getName();

        // then
        assertThat(checkName).isNotBlank();
    }

    @Test
    public void shouldBeHealthy_whenDirectoryIsOk() throws Exception {
        // given
        File jbbDirectory = temp.newFolder("jbbdir");
        given(jbbMetaDataMock.jbbHomePath()).willReturn(jbbDirectory.getAbsolutePath());

        // when
        HealthCheck.Result result = jbbDirectoryHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isTrue();
    }

    @Test
    public void shouldBeUnhealthy_whenDirectoryIsNotFound() throws Exception {
        // given
        File jbbDirectory = temp.newFolder("jbbdir2");
        jbbDirectory.delete();
        given(jbbMetaDataMock.jbbHomePath()).willReturn(jbbDirectory.getAbsolutePath());

        // when
        HealthCheck.Result result = jbbDirectoryHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isFalse();
    }

    @Test
    public void shouldBeUnhealthy_whenDirectoryIsAFile() throws Exception {
        // given
        File jbbDirectory = temp.newFile();
        given(jbbMetaDataMock.jbbHomePath()).willReturn(jbbDirectory.getAbsolutePath());

        // when
        HealthCheck.Result result = jbbDirectoryHealthCheck.check();

        // then
        assertThat(result.isHealthy()).isFalse();
    }

}