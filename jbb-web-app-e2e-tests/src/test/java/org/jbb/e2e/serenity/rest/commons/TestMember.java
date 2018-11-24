/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.commons;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class TestMember {
    private Long memberId;

    private String username;

    private String password;

    public String getDisplayedName() {
        return username;
    }

    public String getEmail() {
        return getDisplayedName().toLowerCase() + "@gmail.com";
    }
}
