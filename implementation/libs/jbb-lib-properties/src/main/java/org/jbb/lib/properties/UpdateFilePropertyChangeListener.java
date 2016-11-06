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


import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

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
                PropertiesConfiguration conf = new PropertiesConfiguration(propertyFile);
                conf.setAutoSave(true);
                evt.setPropagationId(conf.getFile().getName());
                conf.setProperty(evt.getPropertyName(), evt.getNewValue());
            } catch (ConfigurationException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }
}
