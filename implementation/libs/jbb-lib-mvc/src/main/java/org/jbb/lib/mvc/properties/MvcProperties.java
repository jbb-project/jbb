/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.properties;

import org.aeonbits.owner.Config;
import org.jbb.lib.properties.ModuleProperties;

@Config.HotReload
@Config.Sources({"file:${jbb.home}/jbb-lib-mvc.properties"})
public interface MvcProperties extends ModuleProperties { // NOSONAR (key names should stay)
    String LOCAL_DATE_TIME_FORMAT_KEY = "global.local.datetime.format";

    @Key(LOCAL_DATE_TIME_FORMAT_KEY)
    String localDateTimeFormatPattern();
}
