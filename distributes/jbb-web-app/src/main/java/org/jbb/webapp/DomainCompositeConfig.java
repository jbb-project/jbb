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

import org.jbb.frontend.FrontendConfig;
import org.jbb.frontend.web.FrontendWebConfig;
import org.jbb.members.MembersConfig;
import org.jbb.members.web.MembersWebConfig;
import org.jbb.security.SecurityConfig;
import org.jbb.security.web.SecurityWebConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        FrontendConfig.class, FrontendWebConfig.class,
        MembersConfig.class, MembersWebConfig.class,
        SecurityConfig.class, SecurityWebConfig.class
})
class DomainCompositeConfig {
}
