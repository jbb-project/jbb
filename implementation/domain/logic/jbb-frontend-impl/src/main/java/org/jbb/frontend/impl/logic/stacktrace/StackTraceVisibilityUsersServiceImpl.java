/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.logic.stacktrace;


import org.apache.commons.lang3.EnumUtils;
import org.jbb.frontend.api.service.stacktrace.StackTraceVisibilityUsersService;
import org.jbb.frontend.api.service.stacktrace.StackTraceVisibilityUsersValues;
import org.jbb.frontend.impl.logic.stacktrace.strategy.api.StackTraceStrategy;
import org.jbb.frontend.impl.properties.FrontendProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StackTraceVisibilityUsersServiceImpl implements StackTraceVisibilityUsersService {

    @Autowired
    private FrontendProperties properties;

    @Autowired
    private List<StackTraceStrategy> stackTraceStrategyList;

    @Override
    public StackTraceVisibilityUsersValues getPermissionToStackTraceVisibility() {
        return EnumUtils.getEnum(StackTraceVisibilityUsersValues.class, properties.stackTraceVisibilityUsers());
    }
}
