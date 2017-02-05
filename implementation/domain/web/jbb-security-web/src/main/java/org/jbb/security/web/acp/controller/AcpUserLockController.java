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
import org.jbb.security.web.acp.form.MemberViewUserLockDetailsForm;
import org.jbb.security.web.acp.form.UserLockServiceSettingsForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

@Component
@RequestMapping("/acp/general/lock")
public class AcpUserLockController {

    private static final String USER_LOCK_SERVICE_VIEW_NAME = "acp/general/lock";
    private static final String MEMBER_SELECT_EDIT_VIEW_NAME = "acp/members/edit";
    private static final String ACP_USER_LOCK_SETTING_FORM = "settings";
    private static final String MEMBER_SEARCH_SELECT_VIEW_USER_LOCK_DETAILS_FORM = "userAccountLockForm";

    private static final String VIEW_DATA_ATTRIBUTE_NAME = "data";

    private final static String MAXIMUM_INVALID_ATTEMPTS = "Maximum of invalid attempts: ";
    private final static String SERVICE_AVAILABLE = "Does User Lock Service is available: ";
    private final static String MEASUREMENT_TIME_PERIOD = "Time period where attempts are measured: ";
    private final static String ACCOUNT_LOCK_TIME_PERIOD = "How long is account lock: ";

    @Autowired
    private UserLockService userLockService;


    @RequestMapping(method = RequestMethod.GET)
    public String userLockSettingsPanelGet(Model model, @ModelAttribute(ACP_USER_LOCK_SETTING_FORM) UserLockServiceSettingsForm settings) {
        model.addAttribute(VIEW_DATA_ATTRIBUTE_NAME, getData());
        model.addAttribute(ACP_USER_LOCK_SETTING_FORM, new UserLockServiceSettingsForm());
        return USER_LOCK_SERVICE_VIEW_NAME;
    }

    @RequestMapping(value = "/getlock/", method = RequestMethod.GET)
    public String getUserAccountLockDetails(Model model, @RequestParam(value = "id") long userID,
                                            @ModelAttribute(MEMBER_SEARCH_SELECT_VIEW_USER_LOCK_DETAILS_FORM) MemberViewUserLockDetailsForm userAccountLockForm) {

        return MEMBER_SELECT_EDIT_VIEW_NAME;

    }

    @RequestMapping(method = RequestMethod.POST)
    public String userLockSettingsPanelPost(@ModelAttribute(ACP_USER_LOCK_SETTING_FORM) @Valid UserLockServiceSettingsForm settings, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            rejectValues(bindingResult);
            model.addAttribute(VIEW_DATA_ATTRIBUTE_NAME, getData());
            return USER_LOCK_SERVICE_VIEW_NAME;
        }
        UserLockSettings serviceSettings = createSettings(settings);
        userLockService.setProperties(serviceSettings);

        return "redirect:/" + USER_LOCK_SERVICE_VIEW_NAME;
    }

    @RequestMapping(value = "/release/{userID}", method = RequestMethod.DELETE)
    public String releaseLockOnDemand(@PathVariable("userID") Long userID) {
        userLockService.releaseUserAccountLockOnDemand(userID);
        return USER_LOCK_SERVICE_VIEW_NAME;
    }

    private void rejectValues(BindingResult bindingResult) {
        bindingResult.reject("maximumNumberOfInvalidSignInAttempts");
        bindingResult.reject("invalidAttemptsMeasurementTimePeriod");
        bindingResult.reject("accountLockTimePeriod");
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
