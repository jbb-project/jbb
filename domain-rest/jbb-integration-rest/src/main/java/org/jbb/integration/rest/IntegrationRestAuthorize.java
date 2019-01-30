/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.integration.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class IntegrationRestAuthorize {

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('webhook:read', 'webhook:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_WEBHOOK_EVENT_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('webhook:write')";

}
