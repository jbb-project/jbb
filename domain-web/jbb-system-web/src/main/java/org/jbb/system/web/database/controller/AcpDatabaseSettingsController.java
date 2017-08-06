/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.web.database.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.system.api.database.DatabaseConfigException;
import org.jbb.system.api.database.DatabaseSettings;
import org.jbb.system.api.database.DatabaseSettingsService;
import org.jbb.system.web.database.form.DatabaseSettingsForm;
import org.jbb.system.web.database.logic.DatabaseSettingsErrorBindingMapper;
import org.jbb.system.web.database.logic.FormDatabaseTranslator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/acp/system/database")
@Slf4j
@RequiredArgsConstructor
public class AcpDatabaseSettingsController {
    private static final String VIEW_NAME = "acp/system/database";
    private static final String DATABASE_SETTINGS_FORM = "databaseSettingsForm";
    private static final String FORM_SAVED_FLAG = "databaseSettingsFormSaved";

    private final DatabaseSettingsService databaseSettingsService;
    private final DatabaseSettingsErrorBindingMapper errorMapper;
    private final FormDatabaseTranslator formTranslator;

    @RequestMapping(method = RequestMethod.GET)
    public String systemDatabaseSettingsGet(Model model,
                                            @ModelAttribute(DATABASE_SETTINGS_FORM) DatabaseSettingsForm form) {

        DatabaseSettings databaseSettings = databaseSettingsService.getDatabaseSettings();
        formTranslator.fillDatabaseSettingsForm(databaseSettings, form);
        model.addAttribute(DATABASE_SETTINGS_FORM, form);

        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String systemDatabaseSettingsPost(Model model,
                                             @ModelAttribute(DATABASE_SETTINGS_FORM) DatabaseSettingsForm form,
                                             BindingResult bindingResult,
                                             RedirectAttributes redirectAttributes) {

        if (bindingResult.hasErrors()) {
            log.debug("Database settings form error detected: {}", bindingResult.getAllErrors());
            model.addAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }

        try {
            DatabaseSettings currentDatabaseSettings = databaseSettingsService
                .getDatabaseSettings();
            DatabaseSettings databaseSettings = formTranslator
                .buildDatabaseSettings(form, currentDatabaseSettings);
            databaseSettingsService.setDatabaseSettings(databaseSettings);
        } catch (DatabaseConfigException e) {
            log.debug("Error during update database settings: {}", e);
            errorMapper.map(e.getConstraintViolations(), bindingResult);
            model.addAttribute(FORM_SAVED_FLAG, false);
            return VIEW_NAME;
        }

        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);
        return "redirect:/" + VIEW_NAME;
    }
}
