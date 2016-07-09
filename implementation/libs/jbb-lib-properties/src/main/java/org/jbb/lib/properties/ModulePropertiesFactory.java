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

import org.aeonbits.owner.ConfigFactory;

public class ModulePropertiesFactory {
    private final FreshInstallPropertiesCreator propertiesCreator;

    private final UpdateFilePropertyChangeListenerFactoryBean propChangeFactory;

    public ModulePropertiesFactory(FreshInstallPropertiesCreator propertiesCreator, UpdateFilePropertyChangeListenerFactoryBean propChangeFactory) {
        this.propertiesCreator = propertiesCreator;
        this.propChangeFactory = propChangeFactory;
    }

    public <T extends ModuleProperties> T create(Class<? extends T> clazz) {
        propertiesCreator.putDefaultPropertiesIfNeeded(clazz);
        T properties = ConfigFactory.create(clazz);
        properties.addPropertyChangeListener(propChangeFactory.setClass(clazz).getObject());
        return properties;

        // TODO add PropertyChangeListener for logging
    }
}
