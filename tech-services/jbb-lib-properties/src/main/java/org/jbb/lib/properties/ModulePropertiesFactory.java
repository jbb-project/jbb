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

import org.aeonbits.owner.ConfigFactory;
import org.jbb.lib.properties.encrypt.ReencryptionPropertyChangeListener;

public class ModulePropertiesFactory {
    private final FreshInstallPropertiesCreator propertiesCreator;

    private final UpdateFilePropertyChangeListenerFactoryBean propChangeFactory;

    private final LoggingPropertyChangeListener logPropListener;
    private final ReencryptionPropertyChangeListener reencryptPropListener;

    public ModulePropertiesFactory(FreshInstallPropertiesCreator propertiesCreator,
                                   UpdateFilePropertyChangeListenerFactoryBean propChangeFactory,
                                   LoggingPropertyChangeListener logPropListener,
                                   ReencryptionPropertyChangeListener reencryptPropListener) {
        this.propertiesCreator = propertiesCreator;
        this.propChangeFactory = propChangeFactory;
        this.logPropListener = logPropListener;
        this.reencryptPropListener = reencryptPropListener;
    }

    public <T extends ModuleStaticProperties> T create(Class<? extends T> clazz) {
        propertiesCreator.putDefaultPropertiesIfNeeded(clazz);
        T properties = ConfigFactory.create(clazz);
        if (ModuleProperties.class.isAssignableFrom(clazz)) {
            ((ModuleProperties) properties).addPropertyChangeListener(propChangeFactory.setClass(clazz).getObject());
            ((ModuleProperties) properties).addPropertyChangeListener(logPropListener);
            ((ModuleProperties) properties).addPropertyChangeListener(reencryptPropListener);
        }
        return properties;
    }
}
