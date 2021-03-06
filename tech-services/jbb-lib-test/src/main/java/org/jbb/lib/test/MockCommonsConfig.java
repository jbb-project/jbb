/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.test;

import com.google.common.base.Throwables;
import java.io.File;
import java.util.Optional;
import javax.naming.NamingException;
import org.jbb.lib.commons.JndiValueReader;
import org.jbb.lib.commons.web.ClientStackTraceProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

@Configuration
public class MockCommonsConfig {
    public static final String ENCRYPTION_TESTBED_PSWD = "jbbRocks";

    protected String jbbHomePath;

    @Bean
    @Primary
    @DependsOn("simpleNamingContextBuilder")
    public JndiValueReader jndiValueReader() {
        return new JndiValueReader();
    }

    @Bean
    @Primary
    public SimpleNamingContextBuilder simpleNamingContextBuilder() throws NamingException {
        File tempDir = com.google.common.io.Files.createTempDir();
        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        jbbHomePath = tempDir.getAbsolutePath();
        builder.bind("jbb/home", jbbHomePath);
        builder.bind("jbb/pswd", ENCRYPTION_TESTBED_PSWD);
        builder.activate();
        return builder;
    }

    @Bean
    public ClientStackTraceProvider dummyStacktraceForClientProvider() {
        return e -> Optional.of(Throwables.getStackTraceAsString(e));
    }

}
