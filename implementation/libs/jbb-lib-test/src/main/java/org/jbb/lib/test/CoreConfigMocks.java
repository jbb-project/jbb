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

import org.jbb.lib.core.JndiValueReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import java.io.File;

import javax.naming.NamingException;

@Configuration
public class CoreConfigMocks {
    public static final String ECRYPTION_TESTBED_PSWD = "jbbRocks";

    @Bean
    @DependsOn("simpleNamingContextBuilder")
    @Primary
    public JndiValueReader jndiValueReader() {
        return new JndiValueReader();
    }

    @Bean
    public SimpleNamingContextBuilder simpleNamingContextBuilder() throws NamingException {
        File tempDir = com.google.common.io.Files.createTempDir();
        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        builder.bind("jbb/home", tempDir.getAbsolutePath());
        builder.bind("jbb/pswd", ECRYPTION_TESTBED_PSWD);
        builder.activate();
        return builder;
    }

}
