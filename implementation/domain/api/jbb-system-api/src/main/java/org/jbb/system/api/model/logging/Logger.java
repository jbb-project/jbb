/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.api.model.logging;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Logger {
    public static final String ROOT_LOGGER_NAME = "ROOT";

    private LogLevel level;
    private boolean addivity;
    private List<LogAppender> appenders = new ArrayList<>();

}
