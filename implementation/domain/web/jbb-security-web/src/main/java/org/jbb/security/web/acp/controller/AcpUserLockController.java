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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Component
@RequestMapping("/acp/general/lock")
public class AcpUserLockController {

    private static final String VIEW_NAME = "acp/general/lock";

    @Autowired
    private UserLockService userLockService;


    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView userLockSettingsPanelGet() {
        ModelAndView modelAndView = new ModelAndView(VIEW_NAME);
        modelAndView.addObject("data", getData());

        return modelAndView;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String userLockSettingsPanelPost(@Valid UserLockSettings settings, BindingResult bindingResult) {
        if( bindingResult.hasErrors()){
            return VIEW_NAME;
        }
        userLockService.setProperties(settings);

        return "redirect:/"+VIEW_NAME;
    }

    private Map<String, String> getData() {
        HashMap<String, String> data = Maps.newHashMap();
        data.put(UserLockViewColumns.MAXIMUM_INVALID_ATTEMPTS, String.valueOf(userLockService.getUserLockServiceSettings().maximumNumberOfInvalidSignInAttempts()));
        data.put(UserLockViewColumns.SERVICE_AVAILABLE, Boolean.toString(userLockService.getUserLockServiceSettings().serviceAvailable()));
        data.put(UserLockViewColumns.MEASUREMENT_TIME_PERIOD, String.valueOf(userLockService.getUserLockServiceSettings().invalidAttemptsMeasurementTimePeriod()));
        data.put(UserLockViewColumns.ACCOUNT_LOCK_TIME_PERIOD, String.valueOf(userLockService.getUserLockServiceSettings().accountLockTimePeriod()));

        return data;
    }

    private final class UserLockViewColumns {

        public final static String MAXIMUM_INVALID_ATTEMPTS = "Maximum of invalid attempts: ";
        public final static String SERVICE_AVAILABLE = "Does User Lock Service is available: ";
        public final static String MEASUREMENT_TIME_PERIOD = "Time period where attempts are measured: ";
        public final static String ACCOUNT_LOCK_TIME_PERIOD = "How long is account lock: ";

    }
}
