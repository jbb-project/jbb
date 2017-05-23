/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import com.google.common.collect.Lists;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
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

    private static void buildCompletePropertyFile(File propertyFile) {
        if (propertyFile.exists()) {
            PropertiesConfiguration referenceProperties = getReferenceProperties(propertyFile);
            PropertiesConfiguration targetProperties = getTargetProperties(propertyFile);

            addMissingProperties(referenceProperties, targetProperties);
            removeObsoleteProperties(referenceProperties, targetProperties);
        } else {
            getDefaultFromClasspath(propertyFile);
        }
    }

    private static void getDefaultFromClasspath(File propertyFile) {
        ClassPathResource classPathResource = new ClassPathResource(propertyFile.getName());
        try {
            FileUtils.copyURLToFile(classPathResource.getURL(), propertyFile);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static PropertiesConfiguration getReferenceProperties(File propertyFile) {
        try {
            ClassPathResource classPathResource = new ClassPathResource(propertyFile.getName());
            return new PropertiesConfiguration(classPathResource.getURL());
        } catch (ConfigurationException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static PropertiesConfiguration getTargetProperties(File propertyFile) {
        try {
            PropertiesConfiguration targetPropertiesConfig = new PropertiesConfiguration(propertyFile);
            targetPropertiesConfig.setAutoSave(true);
            return targetPropertiesConfig;
        } catch (ConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static void addMissingProperties(PropertiesConfiguration reference, PropertiesConfiguration target) {
        Lists.newArrayList(reference.getKeys()).stream()
                .filter(propertyKey -> !target.containsKey(propertyKey))
                .forEach(missingKey -> target.setProperty(missingKey, reference.getProperty(missingKey)));
    }

    private static void removeObsoleteProperties(PropertiesConfiguration reference, PropertiesConfiguration target) {
        Lists.newArrayList(target.getKeys()).stream()
                .filter(propertyKey -> !reference.containsKey(propertyKey))
                .forEach(obsoleteKey -> target.clearProperty(obsoleteKey));
    }

    public void putDefaultPropertiesIfNeeded(Class<? extends ModuleStaticProperties> clazz) {
        Validate.notNull(clazz, "Class cannot be null");
        Set<String> propertyFiles = resolver.resolvePropertyFileNames(clazz);
        for (String propFileStr : propertyFiles) {
            File propertyFile = new File(propFileStr);
            buildCompletePropertyFile(propertyFile);
        }
    }
}
