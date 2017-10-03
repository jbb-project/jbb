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

import javax.validation.constraints.NotNull;

public class MemberIdentity implements SecurityIdentity {

    @NotNull
    private Long memberId;

    public MemberIdentity(Long memberId) {
        this.memberId = memberId;
    }

    @Override
    public Long getId() {
        return memberId;
    }

    @Override
    public Type getType() {
        return Type.MEMBER;
    }
}
