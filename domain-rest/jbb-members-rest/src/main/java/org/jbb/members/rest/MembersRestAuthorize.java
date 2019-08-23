/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class MembersRestAuthorize {

    public static final String PERMIT_ALL_OR_OAUTH_MEMBER_READ_SCOPE =
            "#api.notOAuthOrHasAnyScope('member:read', 'member:create', 'member:delete', 'member:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_MEMBER_DELETE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('member:delete', 'member:write')";

    public static final String PERMIT_ALL_OR_OAUTH_MEMBER_CREATE_SCOPE =
            "#api.notOAuthOrHasAnyScope('member:create', 'member:write')";

    public static final String IS_AUTHENTICATED_OR_OAUTH_MEMBER_PROFILE_READ_SCOPE =
            "#api.notOAuthButAuthenticatedOrHasAnyScope('member.profile:read', 'member.profile:write')";

    public static final String IS_AUTHENTICATED_OR_OAUTH_MEMBER_PROFILE_READ_WRITE_SCOPE =
            "#api.notOAuthButAuthenticatedOrHasAnyScope('member.profile:write')";

    public static final String IS_AUTHENTICATED_OR_OAUTH_MEMBER_ACCOUNT_READ_SCOPE =
            "#api.notOAuthButAuthenticatedOrHasAnyScope('member.account:read', 'member.account:write')";

    public static final String IS_AUTHENTICATED_OR_OAUTH_MEMBER_ACCOUNT_READ_WRITE_SCOPE =
            "#api.notOAuthButAuthenticatedOrHasAnyScope('member.account:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_REGISTRATION_SETTINGS_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('registration_settings:read', 'registration_settings:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_REGISTRATION_SETTINGS_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('registration_settings:write')";

    public static final String IS_AUTHENTICATED_OR_OAUTH_MEMBER_SSE_STREAM_READ_SCOPE_AND_NOT_CLIENT_ONLY =
            "#api.isAuthenticatedAndNotClientOnlyOrHasAnyScope('member.sse_stream:read')";


}
