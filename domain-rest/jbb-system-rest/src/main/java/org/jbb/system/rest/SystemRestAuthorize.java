/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SystemRestAuthorize {

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_CACHE_SETTINGS_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('cache_settings:read', 'cache_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_CACHE_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('cache_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_DATABASE_SETTINGS_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('database_settings:read', 'database_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_DATABASE_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('database_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_LOGGING_SETTINGS_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('logging_settings:read', 'logging_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_LOGGING_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('logging_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_METRICS_SETTINGS_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('metrics_settings:read', 'metrics_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_METRICS_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('metrics_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_SESSION_SETTINGS_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('session_settings:read', 'session_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_SESSION_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('session_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_SESSION_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('member.session:read', 'member.session:delete')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_SESSION_READ_DELETE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('member.session:delete')";

    public static final String PERMIT_ALL_OR_OAUTH_HEALTH_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('health:read')";

    public static final String PERMIT_ALL_OR_OAUTH_API_ERROR_CODES_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('api_error_codes:read')";

}
