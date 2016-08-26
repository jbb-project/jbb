/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.properties;

import org.aeonbits.owner.Config;
import org.jbb.lib.properties.ModuleProperties;

@Config.Sources({"file:${jbb.home}/jbb-security.properties"})
public interface SecurityProperties extends ModuleProperties { // NOSONAR (key names should stay)
    String PSWD_MIN_LENGTH_KEY = "password.minimum.length";
    String PSWD_MAX_LENGTH_KEY = "password.maximum.length";

    @Key(PSWD_MIN_LENGTH_KEY)
    Integer passwordMinimumLength();

    @Key(PSWD_MAX_LENGTH_KEY)
    Integer passwordMaximumLength();

}
