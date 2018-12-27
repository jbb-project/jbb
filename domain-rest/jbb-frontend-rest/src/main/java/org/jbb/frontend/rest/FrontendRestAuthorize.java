/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class FrontendRestAuthorize {

    public static final String PERMIT_ALL_OR_OAUTH_FAQ_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('faq:read', 'faq:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_FAQ_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('faq:write')";

    public static final String PERMIT_ALL_OR_OAUTH_FORMAT_SETTINGS_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('format_settings:read', 'format_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_FORMAT_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('format_settings:write')";

}
