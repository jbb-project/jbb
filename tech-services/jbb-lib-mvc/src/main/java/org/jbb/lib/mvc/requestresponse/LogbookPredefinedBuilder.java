/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.requestresponse;

import org.slf4j.LoggerFactory;
import org.zalando.logbook.DefaultHttpLogWriter;
import org.zalando.logbook.Logbook;
import org.zalando.logbook.LogbookCreator;

import static org.zalando.logbook.Conditions.requestTo;
import static org.zalando.logbook.HeaderFilters.authorization;
import static org.zalando.logbook.QueryFilters.accessToken;
import static org.zalando.logbook.QueryFilters.replaceQuery;


public class LogbookPredefinedBuilder {

    public LogbookCreator.Builder getBuilder() {
        return Logbook.builder()
                .condition(requestTo("/api/**"))
                .queryFilter(accessToken())
                .queryFilter(replaceQuery("password", "<secret>"))
                .headerFilter(authorization())
                .writer(new DefaultHttpLogWriter(
                        LoggerFactory.getLogger("http.audit"),
                        DefaultHttpLogWriter.Level.TRACE));
    }
}
