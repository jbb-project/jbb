/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api.identity;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RegisteredMembersIdentity implements SecurityIdentity {

    private static final RegisteredMembersIdentity INSTANCE = new RegisteredMembersIdentity();

    public static RegisteredMembersIdentity getInstance() {
        return INSTANCE;
    }

    @Override
    public Long getId() {
        return 0L; //NOSONAR
    }

    @Override
    public Type getType() {
        return Type.REGISTERED_MEMBERS;
    }
}
