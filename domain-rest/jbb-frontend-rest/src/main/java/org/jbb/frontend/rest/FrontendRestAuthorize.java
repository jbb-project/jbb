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
            "#api.notOAuthOrHasAnyScope('FAQ_READ', 'FAQ_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_FAQ_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('FAQ_READ_WRITE')";

    public static final String PERMIT_ALL_OR_OAUTH_FORMAT_SETTINGS_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('FORMAT_SETTINGS_READ', 'FORMAT_SETTINGS_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_FORMAT_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('FORMAT_SETTINGS_READ_WRITE')";

}
