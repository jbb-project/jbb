/*
 * Copyright (C) 2017 the original author or authors.
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
public class MembersRestConstants {

    public static final String MEMBERS = "/members";
    public static final String MEMBER_ID_VAR = "memberId";
    public static final String MEMBER_ID = "/{" + MEMBER_ID_VAR + "}";

    public static final String ACCOUNT = "/account";

    public static final String PROFILE = "/profile";
    public static final String PUBLIC_PROFILE = "/public-profile";

    public static final String REGISTRATION_SETTINGS = "/registration-settings";

}
