/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.api.service.stacktrace;


public enum StackTraceVisibilityUsersValues {

    NOBODY("nobody"), ADMINISTRATORS("administrators"), USERS("users"), EVERYBODY("everybody");

    private String name;

    StackTraceVisibilityUsersValues(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
