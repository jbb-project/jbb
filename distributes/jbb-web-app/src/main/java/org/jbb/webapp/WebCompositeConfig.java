/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.jbb.board.web.BoardWebConfig;
import org.jbb.frontend.web.FrontendWebConfig;
import org.jbb.members.web.MembersWebConfig;
import org.jbb.security.web.SecurityWebConfig;
import org.jbb.system.web.SystemWebConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        FrontendWebConfig.class,
        MembersWebConfig.class,
        SecurityWebConfig.class,
        BoardWebConfig.class,
        SystemWebConfig.class
})
public class WebCompositeConfig {
}
