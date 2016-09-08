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


import org.jbb.frontend.api.service.stacktrace.StackTraceVisibilityUsersService;
import org.jbb.frontend.api.service.stacktrace.StackTraceVisibilityUsersValues;
import org.jbb.frontend.impl.logic.stacktrace.strategy.StackTraceStrategy;
import org.jbb.frontend.impl.properties.FrontendProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StackTraceVisibilityUsersServiceImpl implements StackTraceVisibilityUsersService {

    @Autowired
    private FrontendProperties properties;

    @Autowired
    private List<StackTraceStrategy> stackTraceStrategyList;

    @Override
    public Optional<String> getPermissionToStackTraceVisibility(Exception ex) {
        Optional<String> optionalStackTrace = Optional.empty();


        for (StackTraceStrategy singleStackTraceStrategy : stackTraceStrategyList) {
            if (singleStackTraceStrategy.canHandle(readStackTraceProperties()))
                optionalStackTrace = singleStackTraceStrategy.getStackTraceString(ex.getCause());
        }

        return optionalStackTrace;
    }

    private StackTraceVisibilityUsersValues readStackTraceProperties() {
        return Enum.valueOf(StackTraceVisibilityUsersValues.class, properties.stackTraceVisibilityUsers());
    }

}
