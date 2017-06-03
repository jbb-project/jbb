/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.test;

import org.jbb.lib.commons.H2Settings;
import org.jbb.lib.commons.JndiValueReader;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.io.File;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Configuration
public class MockWithoutJndiCommonsConfig {
    public static final String ECRYPTION_TESTBED_PSWD = "jbbRocks";

    @Bean
    @Primary
    public JndiValueReader jndiValueReader() {
        File tempDir = com.google.common.io.Files.createTempDir();
        JndiValueReader jndiValueReaderMock = Mockito.mock(JndiValueReader.class);
        when(jndiValueReaderMock.readValue(eq("jbb/home"))).thenReturn(tempDir.getAbsolutePath());
        when(jndiValueReaderMock.readValue(eq("jbb/pswd"))).thenReturn(ECRYPTION_TESTBED_PSWD);
        return jndiValueReaderMock;
    }

    @Primary
    @Bean
    H2Settings h2Settings() {
        H2Settings h2Settings = new H2Settings();
        h2Settings.setMode(H2Settings.Mode.FILE);
        return h2Settings;
    }

}
