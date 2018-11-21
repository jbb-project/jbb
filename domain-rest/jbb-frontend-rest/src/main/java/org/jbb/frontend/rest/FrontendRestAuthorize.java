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
            "not #oauth2.isOAuth() or #oauth2.hasAnyScope('FAQ_READ', 'FAQ_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_FAQ_READ_WRITE_SCOPE =
            "(hasRole('ROLE_ADMINISTRATOR') and not #oauth2.isOAuth()) or #oauth2.hasAnyScope('FAQ_READ_WRITE')";

    public static final String PERMIT_ALL_OR_OAUTH_FORMAT_SETTINGS_READ_SCOPE =
            "not #oauth2.isOAuth() or #oauth2.hasAnyScope('FORMAT_SETTINGS_READ', 'FORMAT_SETTINGS_READ_WRITE')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_FORMAT_SETTINGS_READ_WRITE_SCOPE =
            "(hasRole('ROLE_ADMINISTRATOR') and not #oauth2.isOAuth()) or #oauth2.hasAnyScope('FORMAT_SETTINGS_READ_WRITE')";

}