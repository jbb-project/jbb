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

import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.Validate;
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
            PropertiesConfiguration targetProperties = getTargetPropertiesFromJbbPath(propertyFile);

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
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                    new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                            .configure(new Parameters().properties()
                                    .setURL(classPathResource.getURL())
                                    .setThrowExceptionOnMissing(true)
                                    .setIncludesAllowed(false));
            return builder.getConfiguration();
        } catch (ConfigurationException | IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private static PropertiesConfiguration getTargetPropertiesFromJbbPath(File propertyFile) {
        try {
            FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                    new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                            .configure(new Parameters().properties()
                                    .setFile(propertyFile)
                                    .setIncludesAllowed(false));
            builder.setAutoSave(true);
            return builder.getConfiguration();
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
