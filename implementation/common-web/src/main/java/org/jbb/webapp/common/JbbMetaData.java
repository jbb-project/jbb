/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp.common;

import org.aeonbits.owner.Config;
import org.jbb.lib.properties.ModuleProperties;

@Config.Sources({"classpath:manifest.data"})
public interface JbbMetaData extends ModuleProperties {  //NOSONAR
    String JBB_VERSION_KEY = "jbb.version";

    @Key(JBB_VERSION_KEY)
    String jbbVersion();
}
