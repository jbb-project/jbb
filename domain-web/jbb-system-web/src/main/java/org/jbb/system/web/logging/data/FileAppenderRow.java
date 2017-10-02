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

import org.jbb.system.api.logging.model.LogFileAppender;
import org.jbb.system.api.logging.model.LogFilter;
import org.jbb.system.web.logging.logic.FilterUtils;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class FileAppenderRow {
    private String name;
    private String currentLogFileName;
    private String fileNamePattern;
    private String maxFileSize;
    private int maxHistory;
    private String filter;
    private String logPattern;

    public FileAppenderRow(String name, String currentLogFileName, String fileNamePattern,
                           LogFileAppender.FileSize maxFileSize,
                           int maxHistory, LogFilter logFilter, String pattern) {
        this.name = name;
        this.currentLogFileName = currentLogFileName;
        this.fileNamePattern = fileNamePattern;
        this.maxFileSize = maxFileSize.toString();
        this.maxHistory = maxHistory;
        this.filter = FilterUtils.getFilterText(logFilter);
        this.logPattern = pattern;
    }
}
