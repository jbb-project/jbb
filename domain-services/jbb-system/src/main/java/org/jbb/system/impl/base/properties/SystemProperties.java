/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.impl.base.properties;

import org.aeonbits.owner.Config;
import org.jbb.lib.properties.ModuleProperties;

@Config.HotReload(type = Config.HotReloadType.ASYNC)
@Config.Sources({"file:${jbb.home}/jbb-system.properties"})
public interface SystemProperties extends ModuleProperties { // NOSONAR (key names should stay)
    String STACK_TRACE_VISIBILITY_LEVEL_KEY = "stacktrace.visibility.level";
    String SESSION_INACTIVE_INTERVAL_TIME_AS_SECONDS = "session.inactive.interval.time.as.seconds";

    @Key(STACK_TRACE_VISIBILITY_LEVEL_KEY)
    String stackTraceVisibilityLevel();

    @Key(SESSION_INACTIVE_INTERVAL_TIME_AS_SECONDS)
    Integer sessionMaxInActiveTimeAsSeconds();
}