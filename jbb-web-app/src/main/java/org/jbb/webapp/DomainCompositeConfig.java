/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.jbb.board.impl.BoardConfig;
import org.jbb.frontend.impl.FrontendConfig;
import org.jbb.members.impl.MembersConfig;
import org.jbb.permissions.impl.PermissionsConfig;
import org.jbb.posting.impl.PostingConfig;
import org.jbb.security.impl.SecurityConfig;
import org.jbb.system.impl.SystemConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
    PermissionsConfig.class,
    FrontendConfig.class,
    MembersConfig.class,
    SecurityConfig.class,
    BoardConfig.class,
    SystemConfig.class,
    PostingConfig.class})
public class DomainCompositeConfig {

}
