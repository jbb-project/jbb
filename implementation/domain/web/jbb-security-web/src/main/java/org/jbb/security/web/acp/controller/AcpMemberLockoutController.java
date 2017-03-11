/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.acp.controller;

import org.jbb.security.api.model.UserLockSettings;
import org.jbb.security.api.service.UserLockService;
import org.jbb.security.web.acp.form.UserLockSettingsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

@Component
@RequestMapping("/acp/general/lockout")
public class AcpMemberLockoutController {

    private static final String USER_LOCK_SERVICE_VIEW_NAME = "acp/general/lockout";
    private static final String ACP_USER_LOCK_SETTING_FORM = "lockoutSettingsForm";
    private static final String FORM_SAVED_FLAG = "lockoutSettingsFormSaved";

    @Autowired
    private UserLockService userLockService;

    @RequestMapping(method = RequestMethod.GET)
    public String userLockSettingsPanelGet(Model model) {

        UserLockSettings settings = userLockService.getUserLockServiceSettings();

        UserLockSettingsForm form = new UserLockSettingsForm();
        form.setLockingEnabled(settings.serviceAvailable());
        form.setInvalidAttemptsMeasurementTimePeriod(settings.invalidAttemptsMeasurementTimePeriod());
        form.setMaximumNumberOfInvalidSignInAttempts(settings.maximumNumberOfInvalidSignInAttempts());
        form.setAccountLockTimePeriod(settings.accountLockTimePeriod());

        model.addAttribute(ACP_USER_LOCK_SETTING_FORM, form);
        return USER_LOCK_SERVICE_VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String userLockSettingsPanelPost(@ModelAttribute(ACP_USER_LOCK_SETTING_FORM) @Valid UserLockSettingsForm settings,
                                            BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return USER_LOCK_SERVICE_VIEW_NAME;
        }
        UserLockSettings serviceSettings = createSettings(settings);
        userLockService.setProperties(serviceSettings);

        redirectAttributes.addFlashAttribute(FORM_SAVED_FLAG, true);

        return "redirect:/" + USER_LOCK_SERVICE_VIEW_NAME;
    }

    private UserLockSettings createSettings(UserLockSettingsForm settings) {
        return new UserLockSettings() {
            @Override
            public int maximumNumberOfInvalidSignInAttempts() {
                return settings.getMaximumNumberOfInvalidSignInAttempts();
            }

            @Override
            public Long invalidAttemptsMeasurementTimePeriod() {
                return settings.getInvalidAttemptsMeasurementTimePeriod();
            }

            @Override
            public Long accountLockTimePeriod() {
                return settings.getAccountLockTimePeriod();
            }

            @Override
            public boolean serviceAvailable() {
                return settings.isLockingEnabled();
            }
        };
    }

}
