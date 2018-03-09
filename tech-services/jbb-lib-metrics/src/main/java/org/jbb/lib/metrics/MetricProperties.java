/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.metrics;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.Sources;
import org.jbb.lib.properties.ModuleProperties;

@Config.HotReload(type = Config.HotReloadType.ASYNC)
@Sources({"file:${jbb.home}/config/metrics.properties"})
public interface MetricProperties extends ModuleProperties { // NOSONAR (key names should stay)

    String JVM_METRICS_CONSOLE_ENABLED = "jvm.console.enabled";

    @Key(JVM_METRICS_CONSOLE_ENABLED)
    Boolean jvmConsoleEnabled();


}