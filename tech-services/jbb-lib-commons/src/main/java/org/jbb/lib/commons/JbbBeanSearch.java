/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JbbBeanSearch {

    private static final String ROOT_JBB_PACKAGE = "org.jbb";

    private final ApplicationContext applicationContext;

    public <T> List<? extends T> getBeanClasses(Class<? extends T> type) {
        return applicationContext.getBeansOfType(type).values().stream()
            .filter(bean -> bean.getClass().getName().startsWith(ROOT_JBB_PACKAGE))
            .collect(Collectors.toList());
    }


}
