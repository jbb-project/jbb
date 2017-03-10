/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.data;

import org.jbb.system.api.model.logging.LogConsoleAppender;
import org.jbb.system.api.model.logging.LogFilter;
import org.jbb.system.web.logging.logic.FilterUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ConsoleAppenderRow {
    private String name;
    private String target;
    private String filter;
    private String logPattern;
    private boolean useColor;

    public ConsoleAppenderRow(String name, LogConsoleAppender.Target target,
                              LogFilter filter, String pattern, boolean useColor) {
        this.name = name;
        this.target = target.getValue();
        this.filter = FilterUtils.getFilterText(filter);
        this.logPattern = pattern;
        this.useColor = useColor;
    }
}
