/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityRestAuthorize {

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_LOCK_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('member.lock:read', 'member.lock:delete')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_LOCK_READ_DELETE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('member.lock:delete')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_LOCKOUT_SETTINGS_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('lockout_settings:read', 'lockout_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_LOCKOUT_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('lockout_settings:write')";

    public static final String PERMIT_ALL_OR_OAUTH_PSWD_POLICY_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('password_policy:read', 'password_policy:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_PSWD_POLICY_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('password_policy:write')";

    public static final String PERMIT_ALL_OR_OAUTH_ADMINISTRATOR_PRIVILEGE_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('admin_privilege:read', 'admin_privilege:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_ADMINISTRATOR_PRIVILEGE_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('admin_privilege:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_OAUTH_CLIENT_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('oauth_client:read', 'oauth_client:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_OAUTH_CLIENT_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('oauth_client:write')";

    public static final String PERMIT_ALL_OR_OAUTH_SIGN_IN_SETTINGS_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('sign_in_settings:read', 'sign_in_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_SIGN_IN_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('sign_in_settings:write')";

    public static final String PERMIT_ALL_OR_OAUTH_API_SCOPES_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('api_scopes:read')";

}
