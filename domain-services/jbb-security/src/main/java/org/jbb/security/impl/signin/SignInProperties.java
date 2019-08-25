/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.signin;


import org.aeonbits.owner.Config;
import org.jbb.lib.properties.ModuleProperties;

@Config.HotReload(type = Config.HotReloadType.ASYNC)
@Config.Sources({"file:${jbb.home}/config/security.properties"})
public interface SignInProperties extends ModuleProperties { //NOSONAR

    String BASIC_AUTH_ENABLED = "basicAuth.enabled";
    String REMEMBER_ME_TOKEN_VALIDITY_DAYS = "rememberMe.tokenValidityDays";

    @Key(BASIC_AUTH_ENABLED)
    boolean basicAuthEnabled();

    @Key(REMEMBER_ME_TOKEN_VALIDITY_DAYS)
    long rememberMeTokenValidityDays();

}
