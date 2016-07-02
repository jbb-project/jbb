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

public final class ModulePropertiesFactory {
    private static final FreshInstallPropertiesCreator PROP_CREATOR = new FreshInstallPropertiesCreator();

    private ModulePropertiesFactory() {
        throw new UnsupportedOperationException("don't instantiate this class!");
    }

    public static <T extends ModuleProperties> T create(Class<? extends T> clazz) {
        PROP_CREATOR.putDefaultPropertiesIfNeeded(clazz);
        T properties = ConfigFactory.create(clazz);
        properties.addPropertyChangeListener(new UpdateFilePropertyChangeListener(clazz));
        return properties;

        // TODO add PropertyChangeListener for logging
    }
}