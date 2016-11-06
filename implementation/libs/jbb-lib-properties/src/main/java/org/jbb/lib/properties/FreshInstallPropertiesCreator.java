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

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.Validate;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.Set;

class FreshInstallPropertiesCreator {
    private final JbbPropertyFilesResolver resolver;

    FreshInstallPropertiesCreator(JbbPropertyFilesResolver resolver) {
        this.resolver = resolver;
    }

    private static void getDefaultFromClasspath(File propertyFile) {
        ClassPathResource classPathResource = new ClassPathResource(propertyFile.getName());
        try {
            FileUtils.copyURLToFile(classPathResource.getURL(), propertyFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void putDefaultPropertiesIfNeeded(Class<? extends ModuleStaticProperties> clazz) {
        Validate.notNull(clazz, "Class cannot be null");
        Set<String> propertyFiles = resolver.resolvePropertyFileNames(clazz);
        for (String propFileStr : propertyFiles) {
            File propertyFile = new File(propFileStr);
            if (!propertyFile.exists()) {
                getDefaultFromClasspath(propertyFile);
            }
        }
    }
}
