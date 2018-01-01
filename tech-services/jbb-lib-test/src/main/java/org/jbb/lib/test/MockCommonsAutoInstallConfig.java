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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import java.io.File;
import java.io.IOException;

import javax.naming.NamingException;

@Configuration
public class MockCommonsAutoInstallConfig extends MockCommonsConfig {

    @Bean
    @Primary
    @Override
    public SimpleNamingContextBuilder simpleNamingContextBuilder()
            throws NamingException {
        SimpleNamingContextBuilder builder = super.simpleNamingContextBuilder();
        copyAutoInstallFile();
        return builder;
    }

    private void copyAutoInstallFile() {
        String fileName = "jbb-autoinstall.properties";
        ClassPathResource autoInstallFile = new ClassPathResource(fileName);
        File targetFile = new File(jbbHomePath + File.separator + fileName);
        try {
            FileUtils.copyURLToFile(autoInstallFile.getURL(), targetFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
