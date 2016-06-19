/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import com.google.common.base.Throwables;

import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class FreshInstallPropertiesCreator {
    private JbbPropertyFilesResolver propertyFilesResolver = new JbbPropertyFilesResolver();

    public void putDefaultPropertiesIfNeeded(Class<? extends ModuleProperties> clazz) {
        Set<String> propertyFiles = propertyFilesResolver.resolvePropertyFileNames(clazz);
        for (String propertyFileString : propertyFiles) {
            File propertyFile = new File(propertyFileString);
            if (!propertyFile.exists()) {
                getDefaultFromClasspath(propertyFile);
            }
        }
    }

    private void getDefaultFromClasspath(File propertyFile) {
        ClassPathResource classPathResource = new ClassPathResource(propertyFile.getName());
        try {
            FileCopyUtils.copy(classPathResource.getFile(), propertyFile);
        } catch (IOException e) {
            Throwables.propagate(e);
        }
    }
}
