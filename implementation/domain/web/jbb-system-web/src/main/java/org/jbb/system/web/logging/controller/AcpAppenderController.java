/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.controller;

import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LogConsoleAppender;
import org.jbb.system.api.model.logging.LogFileAppender;
import org.jbb.system.api.service.LoggingSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Optional;

@Controller
@RequestMapping("/acp/general/logging/append")
public class AcpAppenderController {
    private static final String CONSOLE_APPENDER_VIEW_NAME = "acp/general/appender-console";
    private static final String FILE_APPENDER_VIEW_NAME = "acp/general/appender-file";
    private static final String APPENDER_FORM = "appenderForm";
    private static final String NEW_APPENDER_STATE = "newAppenderState";
    private static final String FORM_SAVED_FLAG = "appenderFormSaved";

    private final LoggingSettingsService loggingSettingsService;

    @Autowired
    public AcpAppenderController(LoggingSettingsService loggingSettingsService) {
        this.loggingSettingsService = loggingSettingsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String loggerGet(@RequestParam(value = "act") String action,
                            @RequestParam(value = "id", required = false) String appenderName,
                            Model model) {
        if ("newconsole".equals(action)) {
            return CONSOLE_APPENDER_VIEW_NAME;
        } else if ("newfile".equals(action)) {
            return FILE_APPENDER_VIEW_NAME;
        }

        Optional<LogAppender> appender = loggingSettingsService.getAppender(appenderName);
        if (!appender.isPresent()) {
            throw new IllegalStateException("Appender with name '" + appenderName + "' doesn't exist");
        }

        if ("edit".equals(action)) {
            //todo - insert appender to model
            return resolveView(appender.get());
        } else if ("del".equals(action)) {
            loggingSettingsService.deleteAppender(appender.get());
            return "redirect:/acp/general/logging";
        } else {
            throw new IllegalStateException("Incorrect action: " + action);
        }
    }

    private String resolveView(LogAppender appender) {
        if (appender instanceof LogConsoleAppender) {
            return CONSOLE_APPENDER_VIEW_NAME;
        } else if (appender instanceof LogFileAppender) {
            return FILE_APPENDER_VIEW_NAME;
        }
        throw new IllegalArgumentException("Unknown view for appender: " + appender);
    }
}
