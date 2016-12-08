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

import org.apache.commons.lang3.EnumUtils;
import org.jbb.system.api.data.StackTraceVisibilityLevel;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.jbb.system.api.service.LoggingSettingsService;
import org.jbb.system.api.service.StackTraceService;
import org.jbb.system.web.logging.data.ConsoleAppenderRow;
import org.jbb.system.web.logging.data.FileAppenderRow;
import org.jbb.system.web.logging.data.LoggerRow;
import org.jbb.system.web.logging.form.LoggingSettingsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

import static org.apache.commons.lang3.StringUtils.capitalize;
import static org.apache.commons.lang3.StringUtils.lowerCase;

@Controller
@RequestMapping("/acp/general/logging")
public class AcpLoggingController {
    private static final String VIEW_NAME = "acp/general/logging";
    private static final String LOGGING_SETTINGS_FORM = "loggingSettingsForm";
    private static final String VISIBILITY_LEVELS = "visibilityLevels";
    private static final String FORM_SAVED_FLAG = "loggingSettingsFormSaved";

    private static final String CONSOLE_APPENDERS = "consoleAppenders";
    private static final String FILE_APPENDERS = "fileAppenders";
    private static final String LOGGERS = "loggers";


    private final StackTraceService stackTraceService;
    private final LoggingSettingsService loggingSettingsService;

    @Autowired
    public AcpLoggingController(StackTraceService stackTraceService,
                                LoggingSettingsService loggingSettingsService) {
        this.stackTraceService = stackTraceService;
        this.loggingSettingsService = loggingSettingsService;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String generalLoggingGet(Model model,
                                    @ModelAttribute(LOGGING_SETTINGS_FORM) LoggingSettingsForm form) {
        putVisibilityLevelsToModel(model);
        form.setStackTraceVisibilityLevel(
                capitalize(lowerCase(
                        stackTraceService.getCurrentStackTraceVisibilityLevel().toString()
                ))
        );

        LoggingConfiguration loggingConfiguration = loggingSettingsService.getLoggingConfiguration();

        List<ConsoleAppenderRow> consoleAppenderRows = loggingConfiguration.getConsoleAppenders().stream()
                .map(appender -> new ConsoleAppenderRow(
                        appender.getName(), appender.getTarget(), appender.getFilter(), appender.getPattern(), appender.isUseColor()
                ))
                .collect(Collectors.toList());
        model.addAttribute(CONSOLE_APPENDERS, consoleAppenderRows);

        List<FileAppenderRow> fileAppenderRows = loggingConfiguration.getFileAppenders().stream()
                .map(appender -> new FileAppenderRow(
                        appender.getName(), appender.getCurrentLogFileName(), appender.getRotationFileNamePattern(),
                        appender.getMaxFileSize(), appender.getMaxHistory(), appender.getFilter(), appender.getPattern()
                ))
                .collect(Collectors.toList());
        model.addAttribute(FILE_APPENDERS, fileAppenderRows);

        List<LoggerRow> loggerRows = loggingConfiguration.getLoggers().stream()
                .map(logger -> new LoggerRow(
                        logger.getName(), logger.getLevel(), logger.isAddivity(), logger.getAppenders()
                ))
                .collect(Collectors.toList());
        model.addAttribute(LOGGERS, loggerRows);

        form.setDebugLoggingFrameworkMode(loggingConfiguration.isDebugLoggingFrameworkMode());
        form.setShowPackagingData(loggingConfiguration.isShowPackagingData());

        model.addAttribute(LOGGING_SETTINGS_FORM, form);

        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String generalLoggingPost(Model model,
                                     @ModelAttribute(LOGGING_SETTINGS_FORM) LoggingSettingsForm form,
                                     RedirectAttributes redirectAttributes) {
        putVisibilityLevelsToModel(model);
        StackTraceVisibilityLevel level = EnumUtils.getEnum(StackTraceVisibilityLevel.class,
                form.getStackTraceVisibilityLevel().toUpperCase());
        stackTraceService.setStackTraceVisibilityLevel(level);
        loggingSettingsService.enableDebugLoggingFrameworkMode(form.isDebugLoggingFrameworkMode());
        loggingSettingsService.showPackagingData(form.isShowPackagingData());
        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        return "redirect:/" + VIEW_NAME;
    }

    private void putVisibilityLevelsToModel(Model model) {
        model.addAttribute(VISIBILITY_LEVELS,
                EnumUtils.getEnumList(StackTraceVisibilityLevel.class).stream()
                        .map(level -> capitalize(lowerCase(level.toString())))
                        .collect(Collectors.toList())
        );
    }
}
