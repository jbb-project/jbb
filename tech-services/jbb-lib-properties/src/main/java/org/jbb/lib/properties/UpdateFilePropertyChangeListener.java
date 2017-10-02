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


import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.commons.configuration2.builder.FileBasedConfigurationBuilder;
import org.apache.commons.configuration2.builder.fluent.Parameters;
import org.apache.commons.configuration2.ex.ConfigurationException;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

class UpdateFilePropertyChangeListener implements PropertyChangeListener {
    private final Set<String> propFiles;

    public UpdateFilePropertyChangeListener(JbbPropertyFilesResolver resolver,
                                            Class<? extends ModuleStaticProperties> clazz) {
        this.propFiles = resolver.resolvePropertyFileNames(clazz);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (String propertyFile : propFiles) {
            try {
                FileBasedConfigurationBuilder<PropertiesConfiguration> builder =
                        new FileBasedConfigurationBuilder<>(PropertiesConfiguration.class)
                                .configure(new Parameters().properties()
                                        .setFileName(propertyFile)
                                        .setIncludesAllowed(false));
                builder.setAutoSave(true);
                PropertiesConfiguration conf = builder.getConfiguration();
                evt.setPropagationId(propertyFile);
                conf.setProperty(evt.getPropertyName(), evt.getNewValue());
            } catch (ConfigurationException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
