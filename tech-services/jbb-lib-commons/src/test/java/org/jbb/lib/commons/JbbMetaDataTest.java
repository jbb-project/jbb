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

import org.jbb.lib.commons.JbbHomePath;
import org.jbb.lib.commons.JbbMetaData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class JbbMetaDataTest {
    @Mock
    private JbbHomePath jbbHomePathMock;

    @InjectMocks
    private JbbMetaData jbbMetaData;

    @Test
    public void shouldReturnJbbVersionFromManifestData() throws Exception {
        // when
        String jbbVersion = jbbMetaData.jbbVersion();

        // then
        assertThat(jbbVersion).isEqualTo("0.0.0-SNAPSHOT");
    }

    @Test
    public void shouldReturnJbbHomePath() throws Exception {
        // given
        String path = "/tmp";
        when(jbbHomePathMock.getEffective()).thenReturn(path);

        // when
        String jbbHomePath = jbbMetaData.jbbHomePath();

        // then
        assertThat(jbbHomePath).isEqualTo(path);
    }
}