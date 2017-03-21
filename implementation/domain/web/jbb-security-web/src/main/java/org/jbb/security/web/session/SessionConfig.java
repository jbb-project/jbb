/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.session;

import org.jbb.security.web.SecurityWebConfig;
import org.springframework.session.web.context.AbstractHttpSessionApplicationInitializer;
import org.springframework.stereotype.Component;

@Component
public class SessionConfig extends AbstractHttpSessionApplicationInitializer {

    public SessionConfig() {
        super(SecurityWebConfig.class);
    }
}
