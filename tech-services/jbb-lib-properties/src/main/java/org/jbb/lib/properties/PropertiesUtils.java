/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.properties;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PropertiesUtils {

    public Class<? extends ModuleStaticProperties> getUnproxyClass(
        ModuleStaticProperties moduleStaticProperties) {
        return (Class<? extends ModuleStaticProperties>) moduleStaticProperties.getClass()
            .getInterfaces()[0];
    }

}
