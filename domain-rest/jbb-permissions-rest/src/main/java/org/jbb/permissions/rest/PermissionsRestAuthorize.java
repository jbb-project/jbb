/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class PermissionsRestAuthorize {

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_MEMBER_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('permission.member:read', 'permission.member:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_MEMBER_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('permission.member:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_MEMBER_ROLE_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('permission_role.member:read', 'permission_role.member:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_MEMBER_ROLE_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('permission_role.member:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_ADMINISTRATIVE_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('permission.administrative:read', 'permission.administrative:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_ADMINISTRATIVE_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('permission.administrative:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_ADMINISTRATIVE_ROLE_READ_SCOPE =
            "#api.isAdministratorOrHasAnyScope('permission_role.administrative:read', 'permission_role.administrative:write')";

    public static final String IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_ADMINISTRATIVE_ROLE_READ_WRITE_SCOPE =
            "#api.isAdministratorOrHasAnyScope('permission_role.administrative:write')";

}
