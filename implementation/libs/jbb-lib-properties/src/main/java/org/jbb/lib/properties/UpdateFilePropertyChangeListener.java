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

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.Set;

public class UpdateFilePropertyChangeListener implements PropertyChangeListener {
    private JbbPropertyFilesResolver propertyFilesResolver = new JbbPropertyFilesResolver();

    private Set<String> modulePropertyFilesInJbbHome;

    public UpdateFilePropertyChangeListener(Class<? extends ModuleProperties> clazz) {
        modulePropertyFilesInJbbHome = propertyFilesResolver.resolvePropertyFileNames(clazz);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        for (String propertyFile : modulePropertyFilesInJbbHome) {
            try {
                PropertiesConfiguration conf = new PropertiesConfiguration(propertyFile);
                conf.setAutoSave(true);
                conf.setProperty(evt.getPropertyName(), evt.getNewValue());
            } catch (ConfigurationException e) {
                Throwables.propagate(e);
            }
        }
    }
}
