/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.webapp;

import org.jbb.frontend.rest.FrontendRestConfig;
import org.jbb.members.rest.MembersRestConfig;
import org.jbb.permissions.rest.PermissionsRestConfig;
import org.jbb.system.rest.SystemRestConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({
        MembersRestConfig.class,
        PermissionsRestConfig.class,
        SystemRestConfig.class,
        FrontendRestConfig.class
})
class RestCompositeConfig {

}
