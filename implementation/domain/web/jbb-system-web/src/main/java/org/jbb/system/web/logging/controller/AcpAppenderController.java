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

import org.jbb.system.api.model.logging.LogAppender;
import org.jbb.system.api.model.logging.LogConsoleAppender;
import org.jbb.system.api.model.logging.LogFileAppender;
import org.jbb.system.api.service.LoggingSettingsService;
import org.jbb.system.web.logging.form.ConsoleAppenderSettingsForm;
import org.jbb.system.web.logging.logic.FilterUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/acp/general/logging/append")
public class AcpAppenderController {
    private static final List<String> TARGETS = Lists.newArrayList("System.out", "System.err");

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
            putNewAppenderFlag(model);
            ConsoleAppenderSettingsForm form = new ConsoleAppenderSettingsForm();
            form.setAddingMode(true);
            model.addAttribute(NEW_APPENDER_STATE, true);
            model.addAttribute(APPENDER_FORM, form);
            insertTargets(model);
            insertFilters(model);
            return CONSOLE_APPENDER_VIEW_NAME;
        } else if ("newfile".equals(action)) {
            putNewAppenderFlag(model);
            return FILE_APPENDER_VIEW_NAME;
        }

        Optional<LogAppender> appender = loggingSettingsService.getAppender(appenderName);
        if (!appender.isPresent()) {
            throw new IllegalStateException("Appender with name '" + appenderName + "' doesn't exist");
        }

        if ("edit".equals(action)) {
            insertAppenderToView(appender.get(), model);
            return resolveView(appender.get());
        } else if ("del".equals(action)) {
            loggingSettingsService.deleteAppender(appender.get());
            return "redirect:/acp/general/logging";
        } else {
            throw new IllegalStateException("Incorrect action: " + action);
        }
    }

    private void putNewAppenderFlag(Model model) {
        model.addAttribute(NEW_APPENDER_STATE, true);
    }

    private void insertAppenderToView(LogAppender appender, Model model) {
        model.addAttribute(NEW_APPENDER_STATE, false);
        if (appender instanceof LogConsoleAppender) {
            ConsoleAppenderSettingsForm form = new ConsoleAppenderSettingsForm();
            form.setName(appender.getName());
            form.setTarget(((LogConsoleAppender) appender).getTarget().getValue());
            form.setFilter(FilterUtils.getFilterText(((LogConsoleAppender) appender).getFilter()));
            form.setLogPattern(((LogConsoleAppender) appender).getPattern());
            form.setUseColor(((LogConsoleAppender) appender).isUseColor());
            form.setAddingMode(false);
            model.addAttribute(APPENDER_FORM, form);
            insertTargets(model);
            insertFilters(model);
        }
    }

    private void insertFilters(Model model) {
        model.addAttribute("filters", FilterUtils.getAllFiltersList());
    }

    private void insertTargets(Model model) {
        model.addAttribute("targets", TARGETS);
    }

    private String resolveView(LogAppender appender) {
        if (appender instanceof LogConsoleAppender) {
            return CONSOLE_APPENDER_VIEW_NAME;
        } else if (appender instanceof LogFileAppender) {
            return FILE_APPENDER_VIEW_NAME;
        }
        throw new IllegalArgumentException("Unknown view for appender: " + appender);
    }

    @RequestMapping(path = "/console", method = RequestMethod.POST)
    public String consoleAppenderPost(@ModelAttribute(APPENDER_FORM) ConsoleAppenderSettingsForm form,
                                      RedirectAttributes redirectAttributes) {
        LogConsoleAppender consoleAppender = new LogConsoleAppender();
        consoleAppender.setName(form.getName());
        consoleAppender.setTarget(LogConsoleAppender.Target.getFromStreamName(form.getTarget()));
        consoleAppender.setFilter(FilterUtils.getFilterFromString(form.getFilter()));
        consoleAppender.setPattern(form.getLogPattern());
        consoleAppender.setUseColor(form.isUseColor());
        if (form.isAddingMode()) {
            loggingSettingsService.addAppender(consoleAppender);
        } else {
            loggingSettingsService.updateAppender(consoleAppender);
        }
        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        redirectAttributes.addAttribute("act", "edit");
        redirectAttributes.addAttribute("id", consoleAppender.getName());
        return "redirect:/acp/general/logging/append";
    }
}
