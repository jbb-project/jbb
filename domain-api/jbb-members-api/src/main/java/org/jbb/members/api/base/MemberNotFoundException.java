/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.base;

import lombok.Getter;

public class MemberNotFoundException extends Exception {

    @Getter
    private final Long memberId;

    public MemberNotFoundException() {
        super("Member not found");
        this.memberId = null;
    }

    public MemberNotFoundException(Long memberId) {
        super("Member with id " + memberId + " not found");
        this.memberId = memberId;
    }


}
