/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.logic;


import org.jbb.system.api.model.logging.LogFilter;
import org.jbb.system.api.model.logging.LogLevelFilter;
import org.jbb.system.api.model.logging.LogThresholdFilter;

public final class FilterUtils {
    public static String getFilterText(LogFilter filter) {
        if (filter == null) {
            return "None";
        } else if (filter instanceof LogLevelFilter) {
            return "Level: " + ((LogLevelFilter) filter).getLogLevel().toString().toLowerCase();
        } else if (filter instanceof LogThresholdFilter) {
            return "Threshold: " + ((LogThresholdFilter) filter).getLogLevel().toString().toLowerCase();
        } else {
            return filter.toString();
        }
    }
}
