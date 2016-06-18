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

public final class ModuleConfigFactory {
    private static final SystemProperties SYSTEM_PROPERTIES = ConfigFactory.create(
            SystemProperties.class, System.getProperties(), System.getenv());

    private ModuleConfigFactory() {
        // util class...
    }

    public static <T extends ModuleConfig> T create(Class<? extends T> clazz) {
        return ConfigFactory.create(clazz);
        // TODO add PropertyChangeListener for logging
    }

    public static SystemProperties systemProperties() {
        return SYSTEM_PROPERTIES;
    }
}
