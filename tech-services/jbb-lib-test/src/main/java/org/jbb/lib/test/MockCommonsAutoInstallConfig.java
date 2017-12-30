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

import org.apache.commons.io.FileUtils;
import org.jbb.lib.commons.JndiValueReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import java.io.File;
import java.io.IOException;

import javax.naming.NamingException;

@Configuration
public class MockCommonsAutoInstallConfig {

    public static final String ECRYPTION_TESTBED_PSWD = "jbbRocks";

    @Primary
    @Bean
    @DependsOn("simpleNamingContextBuilder")
    public JndiValueReader jndiValueReader() {
        return new JndiValueReader();
    }

    @Bean
    @Primary
    public SimpleNamingContextBuilder simpleNamingContextBuilder()
            throws NamingException, IOException {
        File tempDir = com.google.common.io.Files.createTempDir();
        SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
        builder.bind("jbb/home", tempDir.getAbsolutePath());
        builder.bind("jbb/pswd", ECRYPTION_TESTBED_PSWD);
        builder.activate();
        copyAutoInstallFile(tempDir.getAbsolutePath());
        return builder;
    }

    private void copyAutoInstallFile(String jbbPath) throws IOException {
        String fileName = "jbb-autoinstall.properties";
        ClassPathResource autoInstallFile = new ClassPathResource(fileName);
        File targetFile = new File(jbbPath + File.separator + fileName);
        FileUtils.copyURLToFile(autoInstallFile.getURL(), targetFile);
    }

}
