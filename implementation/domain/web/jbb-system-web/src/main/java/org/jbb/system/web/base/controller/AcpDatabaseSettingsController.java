/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.base.controller;

import org.jbb.system.api.exception.DatabaseConfigException;
import org.jbb.system.api.model.DatabaseSettings;
import org.jbb.system.api.service.DatabaseSettingsService;
import org.jbb.system.web.base.data.FormDatabaseSettings;
import org.jbb.system.web.base.form.DatabaseSettingsForm;
import org.jbb.system.web.base.logic.DatabaseSettingsErrorBindingMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/acp/system/database")
@Slf4j
public class AcpDatabaseSettingsController {
    private static final String VIEW_NAME = "acp/system/database";
    private static final String DATABASE_SETTINGS_FORM = "databaseSettingsForm";
    private static final String FORM_SAVED_FLAG = "databaseSettingsFormSaved";
    private static final String RESTART_NEEDED_FLAG = "restartNeeded";

    private final DatabaseSettingsService databaseSettingsService;
    private final DatabaseSettingsErrorBindingMapper errorMapper;

    @Autowired
    public AcpDatabaseSettingsController(DatabaseSettingsService databaseSettingsService,
                                         DatabaseSettingsErrorBindingMapper errorMapper) {
        this.databaseSettingsService = databaseSettingsService;
        this.errorMapper = errorMapper;
    }

    @RequestMapping(method = RequestMethod.GET)
    public String systemDatabaseSettingsGet(Model model,
                                            @ModelAttribute(DATABASE_SETTINGS_FORM) DatabaseSettingsForm form) {
        DatabaseSettings databaseSettings = databaseSettingsService.getDatabaseSettings();

        form.setDatabaseFileName(databaseSettings.databaseFileName());
        form.setMinimumIdleConnections(databaseSettings.minimumIdleConnections());
        form.setMaximumPoolSize(databaseSettings.maximumPoolSize());
        form.setConnectionTImeOutMilliseconds(databaseSettings.connectionTimeoutMilliseconds());
        form.setFailAtStartingImmediately(databaseSettings.failAtStartingImmediately());
        form.setDropDatabaseAtStart(databaseSettings.dropDatabaseAtStart());

        model.addAttribute(DATABASE_SETTINGS_FORM, form);
        model.addAttribute(RESTART_NEEDED_FLAG, databaseSettingsService.restartNeeded());

        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String systemDatabaseSettingsPost(Model model,
                                             @ModelAttribute(DATABASE_SETTINGS_FORM) DatabaseSettingsForm form,
                                             BindingResult bindingResult,
                                             RedirectAttributes redirectAttributes) {
        model.addAttribute(RESTART_NEEDED_FLAG, databaseSettingsService.restartNeeded());

        if (bindingResult.hasErrors()) {
            log.debug("Database settings form error detected: {}", bindingResult.getAllErrors());
            redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }

        try {
            DatabaseSettings databaseSettings = new FormDatabaseSettings(form);
            databaseSettingsService.setDatabaseSettings(databaseSettings);
        } catch (DatabaseConfigException e) {
            log.debug("Error during update database settings: {}", e);
            errorMapper.map(e.getConstraintViolations(), bindingResult);
            redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }

        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        return "redirect:/" + VIEW_NAME;
    }
}
