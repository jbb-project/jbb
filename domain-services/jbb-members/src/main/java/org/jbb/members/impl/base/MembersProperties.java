/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base;

import org.aeonbits.owner.Config;
import org.jbb.lib.properties.ModuleProperties;

@Config.HotReload(type = Config.HotReloadType.ASYNC)
@Config.Sources({"file:${jbb.home}/config/members.properties"})
public interface MembersProperties extends ModuleProperties { // NOSONAR (key names should stay)
    String EMAIL_DUPLICATION_KEY = "email.duplication.allowed";

    @Key(EMAIL_DUPLICATION_KEY)
    boolean allowEmailDuplication();
}
