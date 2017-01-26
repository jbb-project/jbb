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

import com.google.common.collect.Maps;

import org.jbb.security.api.model.UserLockSettings;
import org.jbb.security.api.service.UserLockService;
import org.jbb.security.web.acp.form.UserLockServiceSettingsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Map;

@Component
@RequestMapping("/acp/general/lock")
public class AcpUserLockController {

    private static final String VIEW_NAME = "acp/general/lock";
    private static final String SETTING_FORM = "settings";

    private final static String MAXIMUM_INVALID_ATTEMPTS = "Maximum of invalid attempts: ";
    private final static String SERVICE_AVAILABLE = "Does User Lock Service is available: ";
    private final static String MEASUREMENT_TIME_PERIOD = "Time period where attempts are measured: ";
    private final static String ACCOUNT_LOCK_TIME_PERIOD = "How long is account lock: ";

    @Autowired
    private UserLockService userLockService;


    @RequestMapping(method = RequestMethod.GET)
    public String userLockSettingsPanelGet(Model model, @ModelAttribute(SETTING_FORM) UserLockServiceSettingsForm settings) {
        model.addAttribute("data", getData());
        model.addAttribute(SETTING_FORM, new UserLockServiceSettingsForm());
        return VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String userLockSettingsPanelPost(@ModelAttribute("settings") UserLockServiceSettingsForm settings, BindingResult bindingResult, RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            return VIEW_NAME;
        }
        UserLockSettings serviceSettings = createSettings(settings);
        userLockService.setProperties(serviceSettings);

        return "redirect:/" + VIEW_NAME;
    }

    private Map<String, String> getData() {
        HashMap<String, String> data = Maps.newHashMap();
        data.put(MAXIMUM_INVALID_ATTEMPTS, String.valueOf(userLockService.getUserLockServiceSettings().maximumNumberOfInvalidSignInAttempts()));
        data.put(SERVICE_AVAILABLE, Boolean.toString(userLockService.getUserLockServiceSettings().serviceAvailable()));
        data.put(MEASUREMENT_TIME_PERIOD, String.valueOf(userLockService.getUserLockServiceSettings().invalidAttemptsMeasurementTimePeriod()));
        data.put(ACCOUNT_LOCK_TIME_PERIOD, String.valueOf(userLockService.getUserLockServiceSettings().accountLockTimePeriod()));

        return data;
    }

    private UserLockSettings createSettings(UserLockServiceSettingsForm settings) {
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
                return settings.isServiceAvailable();
            }
        };
    }

}
