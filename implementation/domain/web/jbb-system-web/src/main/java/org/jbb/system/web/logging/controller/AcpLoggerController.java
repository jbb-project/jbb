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

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import org.apache.commons.lang3.EnumUtils;
import org.jbb.system.api.exception.LoggingConfigurationException;
import org.jbb.system.api.model.logging.AppLogger;
import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LogConsoleAppender;
import org.jbb.system.api.model.logging.LogFileAppender;
import org.jbb.system.api.model.logging.LogLevel;
import org.jbb.system.api.model.logging.LoggingConfiguration;
import org.jbb.system.api.service.LoggingSettingsService;
import org.jbb.system.web.logging.form.LoggerForm;
import org.jbb.system.web.logging.logic.SimpleErrorsBindingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
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
    private final SimpleErrorsBindingMapper errorsBindingMapper;

    @Autowired
    public AcpLoggerController(LoggingSettingsService loggingSettingsService,
                               SimpleErrorsBindingMapper errorsBindingMapper) {
        this.loggingSettingsService = loggingSettingsService;
        this.errorsBindingMapper = errorsBindingMapper;
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
                             Model model,
                             BindingResult bindingResult,
                             RedirectAttributes redirectAttributes) {
        AppLogger appLogger = new AppLogger();
        appLogger.setName(form.getName());
        appLogger.setAddivity(form.isAddivity());
        appLogger.setLevel(LogLevel.valueOf(form.getLevel().toUpperCase()));
        appLogger.setAppenders(getLogAppenders(form));
        try {
            if (form.isAddingMode()) {
                loggingSettingsService.addLogger(appLogger);
            } else {
                loggingSettingsService.updateLogger(appLogger);
            }

            redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        } catch (LoggingConfigurationException e) {
            errorsBindingMapper.map(e.getConstraintViolations(), bindingResult);
            model.addAttribute(FORM_SAVED_FLAG, false);
            model.addAttribute(LOGGER_FORM, form);
            model.addAttribute(NEW_LOGGER_STATE, form.isAddingMode());
            putLoggingLevelsToModel(model);
            return VIEW_NAME;
        }
        redirectAttributes.addAttribute("act", "edit");
        redirectAttributes.addAttribute("id", appLogger.getName());
        return "redirect:/acp/general/logging/logger";
    }

    private List<LogAppender> getLogAppenders(LoggerForm form) {
        LoggingConfiguration loggingConfiguration = loggingSettingsService.getLoggingConfiguration();

        List<LogConsoleAppender> enabledConsoleAppenders = loggingConfiguration.getConsoleAppenders().stream()
                .filter(appender -> selected(appender, form))
                .collect(Collectors.toList());

        List<LogFileAppender> enabledFileAppenders = loggingConfiguration.getFileAppenders().stream()
                .filter(appender -> selected(appender, form))
                .collect(Collectors.toList());

        List<LogAppender> allEnabledAppenders = Lists.newArrayList();
        allEnabledAppenders.addAll(enabledConsoleAppenders);
        allEnabledAppenders.addAll(enabledFileAppenders);

        return allEnabledAppenders;
    }

    private boolean selected(LogAppender appender, LoggerForm form) {
        return form.getAppenders().containsKey(appender.getName())
                && form.getAppenders().get(appender.getName()).equals(true);
    }
}
