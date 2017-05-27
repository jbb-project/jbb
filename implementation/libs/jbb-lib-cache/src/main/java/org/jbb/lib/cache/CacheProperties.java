/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.cache;

import org.aeonbits.owner.Config;
import org.jbb.lib.properties.ModuleProperties;

@Config.HotReload(type = Config.HotReloadType.ASYNC)
@Config.Sources({"file:${jbb.home}/jbb-lib-cache.properties"})
public interface CacheProperties extends ModuleProperties { // NOSONAR (key names should stay)
    String APPLICATION_CACHE_ENABLED = "cache.application.enabled";
    String LEVEL2_CACHE_ENABLED = "cache.level2.enabled";
    String QUERY_CACHE_ENABLED = "cache.query.enabled";

    @Key(APPLICATION_CACHE_ENABLED)
    Boolean applicationCacheEnabled();

    @Key(LEVEL2_CACHE_ENABLED)
    Boolean level2CacheEnabled();

    @Key(QUERY_CACHE_ENABLED)
    Boolean queryCacheEnabled();
}
