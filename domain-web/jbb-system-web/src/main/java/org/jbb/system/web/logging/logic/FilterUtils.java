/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.logic;


import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbb.system.api.logging.model.LogFilter;
import org.jbb.system.api.logging.model.LogLevel;
import org.jbb.system.api.logging.model.LogLevelFilter;
import org.jbb.system.api.logging.model.LogThresholdFilter;

import java.util.List;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FilterUtils {
    private static final List<String> ALL_FILTERS_LIST =
            Lists.newArrayList("None",
                    "Level: all", "Level: trace", "Level: debug", "Level: info", "Level: warn", "Level: error", "Level: off",
                    "Threshold: all", "Threshold: trace", "Threshold: debug", "Threshold: info", "Threshold: warn", "Threshold: error", "Threshold: off"
            );

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

    public static LogFilter getFilterFromString(String logFilterString) {
        if (logFilterString == null) {
            return null;
        }

        if (StringUtils.isEmpty(logFilterString) ||
                "None".equalsIgnoreCase(logFilterString)) {
            return null;
        }

        if (logFilterString.startsWith("Level: ")) {
            return new LogLevelFilter(EnumUtils.getEnum(LogLevel.class, logFilterString.substring(7).toUpperCase()));
        } else if (logFilterString.startsWith("Threshold: ")) {
            return new LogThresholdFilter(EnumUtils.getEnum(LogLevel.class, logFilterString.substring(11).toUpperCase()));
        }

        return null;
    }

    public static List<String> getAllFiltersList() {
        return ImmutableList.copyOf(ALL_FILTERS_LIST);
    }
}
