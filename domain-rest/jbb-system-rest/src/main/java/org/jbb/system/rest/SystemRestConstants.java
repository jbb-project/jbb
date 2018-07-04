/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SystemRestConstants {

    public static final String API_ERROR_CODES = "/api-error-codes";

    public static final String CACHE_SETTINGS = "/cache-settings";

    public static final String DATABASE_SETTINGS = "/database-settings";

    public static final String ERR = "/err";

    public static final String HEALTH = "/health";

    public static final String INSTALLATION = "/installation";

    public static final String LOGGERS = "/loggers";
    public static final String LOGGER_NAME_VAR = "loggerName";
    public static final String LOGGER_NAME = "/{" + LOGGER_NAME_VAR + "}";

    public static final String LOG_APPENDERS = "/log-appenders";
    public static final String CONSOLE_APPENDERS = "/console-appenders";
    public static final String FILE_APPENDERS = "/file-appenders";
    public static final String APPENDER_NAME_VAR = "appenderName";
    public static final String APPENDER_NAME = "/{" + APPENDER_NAME_VAR + "}";

    public static final String LOGGING_SETTINGS = "/logging-settings";

    public static final String MEMBER_SESSIONS = "/member-sessions";
    public static final String METRIC_SETTINGS = "/metric-settings";

    public static final String SESSION_ID_VAR = "sessionId";
    public static final String SESSION_ID = "/{" + SESSION_ID_VAR + "}";
    public static final String SESSION_SETTINGS = "/session-settings";

    public static final String STATUS = "/status";

}
