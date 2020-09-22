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
public class PermissionsRestConstants {

    public static final String ADMINISTRATIVE_PERMISSIONS = "/administrative-permissions";
    public static final String MEMBER_PERMISSIONS = "/member-permissions";

    public static final String ROLES = "/roles";

    public static final String ROLE_ID_VAR = "roleId";
    public static final String ROLE_ID = "/{" + ROLE_ID_VAR + "}";

    public static final String POSITION = "/position";
    public static final String EFFECTIVES = "/effectives";
    public static final String DEFINITIONS = "/definitions";

}
