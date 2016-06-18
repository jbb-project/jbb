/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.aeonbits.owner.Config;
import org.aeonbits.owner.Config.HotReload;
import org.aeonbits.owner.Config.LoadPolicy;
import org.aeonbits.owner.Config.Sources;
import org.jbb.lib.properties.ModuleConfig;

@HotReload
@LoadPolicy(Config.LoadType.MERGE)
@Sources({"file:${user.home}/jbb/jbb-webapp.properties", "file:${JBB_HOME}/jbb-webapp.properties"})
public interface BasicProperties extends ModuleConfig {  //NOSONAR
    String BOARD_TITLE_KEY = "board.title";

    @Key(BOARD_TITLE_KEY)
    String boardTitle();
}
