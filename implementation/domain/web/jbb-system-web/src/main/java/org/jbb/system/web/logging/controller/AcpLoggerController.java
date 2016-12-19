/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.controller;

import com.google.common.collect.Maps;

import org.apache.commons.lang3.EnumUtils;
import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LogConsoleAppender;
import org.jbb.system.api.model.logging.LogFileAppender;
import org.jbb.system.api.model.logging.LogLevel;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.jbb.system.api.service.LoggingSettingsService;
import org.jbb.system.web.logging.form.LoggerForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.lowerCase;

@Controller
@RequestMapping("/acp/general/logging/logger")
public class AcpLoggerController {
    private static final String VIEW_NAME = "acp/general/logger";
    private static final String LOGGER_FORM = "loggerForm";
    private static final String NEW_LOGGER_STATE = "newLoggerState";
    private static final String LOGGING_LEVELS = "loggingLevels";
    private static final String FORM_SAVED_FLAG = "loggerFormSaved";

    private final LoggingSettingsService loggingSettingsService;

    @Autowired
    public AcpLoggerController(LoggingSettingsService loggingSettingsService) {
        this.loggingSettingsService = loggingSettingsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String loggerGet(@RequestParam(value = "act") String action,
                            @RequestParam(value = "id", required = false) String loggerName,
                            Model model) {
        LoggingConfiguration loggingConfiguration = loggingSettingsService.getLoggingConfiguration();

        AppLogger logger = loggingConfiguration.getLoggers().stream()
                .filter(l -> l.getName().equals(loggerName))
                .findFirst()
                .orElse(new AppLogger());

        if ("new".equals(action)) {
            model.addAttribute(NEW_LOGGER_STATE, true);
            LoggerForm loggerForm = new LoggerForm();
            loggerForm.setAddingMode(true);
            loggerForm.setAppenders(prepareAppendersMap(loggingConfiguration, logger));
            model.addAttribute(LOGGER_FORM, loggerForm);
            putLoggingLevelsToModel(model);
            return VIEW_NAME;
        } else if ("edit".equals(action)) {
            LoggerForm loggerForm = new LoggerForm();
            loggerForm.setName(logger.getName());
            loggerForm.setAddivity(logger.isAddivity());
            loggerForm.setLevel(logger.getLevel().toString().toLowerCase());
            loggerForm.setAppenders(prepareAppendersMap(loggingConfiguration, logger));
            loggerForm.setAddingMode(false);
            model.addAttribute(LOGGER_FORM, loggerForm);
            model.addAttribute(NEW_LOGGER_STATE, false);
            putLoggingLevelsToModel(model);
            return VIEW_NAME;
        } else if ("del".equals(action)) {
            loggingSettingsService.deleteLogger(logger);
            return "redirect:/acp/general/logging";
        } else {
            throw new IllegalStateException("Incorrect action: " + action);
        }
    }

    private Map<String, Boolean> prepareAppendersMap(LoggingConfiguration loggingConfiguration, AppLogger appLogger) {
        AppLogger targetLogger = Optional.ofNullable(appLogger).orElse(new AppLogger());

        Map<String, Boolean> consoleAppenders = loggingConfiguration.getConsoleAppenders().stream()
                .collect(Collectors.toMap(
                        LogConsoleAppender::getName,
                        appender -> targetLogger.getAppenders().stream()
                                .filter(app -> app.getName().equals(appender.getName()))
                                .findFirst().isPresent()
                ));

        Map<String, Boolean> fileAppenders = loggingConfiguration.getFileAppenders().stream()
                .collect(Collectors.toMap(
                        LogFileAppender::getName,
                        appender -> targetLogger.getAppenders().stream()
                                .filter(app -> app.getName().equals(appender.getName()))
                                .findFirst().isPresent()
                ));

        Map<String, Boolean> result = Maps.newTreeMap();
        result.putAll(consoleAppenders);
        result.putAll(fileAppenders);

        return result;
    }

    private void putLoggingLevelsToModel(Model model) {
        model.addAttribute(LOGGING_LEVELS,
                EnumUtils.getEnumList(LogLevel.class).stream()
                        .map(level -> capitalize(lowerCase(level.toString())))
                        .collect(Collectors.toList())
        );
    }

    @RequestMapping(method = RequestMethod.POST)
    public String loggerPost(@ModelAttribute(LOGGER_FORM) LoggerForm form,
                             Model model) {
        //TODO
        throw new UnsupportedOperationException();
    }
}
