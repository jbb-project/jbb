/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db;

import org.aeonbits.owner.Config.Sources;
import org.jbb.lib.properties.ModuleStaticProperties;

@Sources({"file:${jbb.home}/jbb-lib-db-tier-STATIC.properties"})
public interface DbStaticProperties extends ModuleStaticProperties {
    String DB_FILENAME_KEY = "database.filename";
    String DB_MIN_IDLE_KEY = "database.minimum.idle";
    String DB_MAX_POOL_KEY = "database.maximum.pool";
    String DB_CONN_TIMEOUT_MS_KEY = "database.connection.timeout.ms";
    String DB_INIT_FAIL_FAST_KEY = "database.init.fail.fast";
    String DB_DROP_DURING_START_KEY = "database.drop.during.start";

    @Key(DB_FILENAME_KEY)
    String dbFilename();

    @Key(DB_MIN_IDLE_KEY)
    int minimumIdle();

    @Key(DB_MAX_POOL_KEY)
    int maxPool();

    @Key(DB_CONN_TIMEOUT_MS_KEY)
    int connectionTimeoutMiliseconds();

    @Key(DB_INIT_FAIL_FAST_KEY)
    boolean failFastDuringInit();

    @Key(DB_DROP_DURING_START_KEY)
    boolean dropDbDuringStart();
}
