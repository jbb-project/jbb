/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.jbb.lib.core.JbbMetaData;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;

import lombok.extern.slf4j.Slf4j;

import static org.mockito.Mockito.when;

@Configuration
@Slf4j
public class CoreConfigMocks {

    @Bean
    @Primary
    public JbbMetaData jbbMetaData() {
        JbbMetaData metaDataMock = Mockito.mock(JbbMetaData.class);
        File tempDir = com.google.common.io.Files.createTempDir();
        when(metaDataMock.jbbHomePath()).thenReturn(tempDir.getAbsolutePath());
        System.setProperty("jbb.home", tempDir.getAbsolutePath());
        return metaDataMock;
    }
}
