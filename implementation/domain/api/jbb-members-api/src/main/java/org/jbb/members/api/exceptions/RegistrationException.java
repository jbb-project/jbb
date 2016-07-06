/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.exceptions;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.util.Map;

public class RegistrationException extends MemberException {
    private Map<Object, String> errors = Maps.newHashMap();

    public RegistrationException() {
        super(String.format("Login '%s' is busy", null));
    }

    public void putError(Object obj, String msg) {
        errors.put(obj, msg);
    }

    public Map<Object, String> getErrors() {
        return ImmutableMap.copyOf(errors);
    }
}
