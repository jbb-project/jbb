/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.api.oauth;

import java.util.Arrays;
import java.util.Optional;

public enum GrantType {
    REFRESH_TOKEN("refresh_token"),
    AUTHORIZATION_CODE("authorization_code"),
    IMPLICIT("implicit"),
    PASSWORD("password"),
    CLIENT_CREDENTIALS("client_credentials");

    private final String name;

    GrantType(String name) {
        this.name = name;
    }

    public static Optional<GrantType> ofName(String name) {
        return Arrays.stream(GrantType.values())
                .filter(grant -> grant.getName().equals(name))
                .findFirst();
    }

    public String getName() {
        return name;
    }

}
