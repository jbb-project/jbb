/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.logging.controller;

import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jbb.system.api.logging.LoggingSettingsService;
import org.jbb.system.api.logging.model.LoggingConfiguration;
import org.jbb.system.web.logging.data.ConsoleAppenderRow;
import org.jbb.system.web.logging.data.FileAppenderRow;
import org.jbb.system.web.logging.data.LoggerRow;
import org.jbb.system.web.logging.form.LoggingSettingsForm;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/acp/general/logging")
public class AcpLoggingController {
    private static final String VIEW_NAME = "acp/general/logging";
    private static final String LOGGING_SETTINGS_FORM = "loggingSettingsForm";
    private static final String FORM_SAVED_FLAG = "loggingSettingsFormSaved";

    private static final String CONSOLE_APPENDERS = "consoleAppenders";
    private static final String FILE_APPENDERS = "fileAppenders";
    private static final String LOGGERS = "loggers";

    private final LoggingSettingsService loggingSettingsService;

    @RequestMapping(method = RequestMethod.GET)
    public String generalLoggingGet(Model model,
                                    @ModelAttribute(LOGGING_SETTINGS_FORM) LoggingSettingsForm form) {
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
        loggingSettingsService.enableDebugLoggingFrameworkMode(form.isDebugLoggingFrameworkMode());
        loggingSettingsService.showPackagingData(form.isShowPackagingData());
        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        return "redirect:/" + VIEW_NAME;
    }

}
