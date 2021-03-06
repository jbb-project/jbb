/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.controller;

import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.members.api.registration.RegistrationMetaData;
import org.jbb.members.api.registration.RegistrationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UcpStatisticsController {
    private final RegistrationService registrationService;

    @RequestMapping(value = "/ucp/overview/statistics")
    public String statistics(Model model, Authentication authentication) {
        SecurityContentUser currentUser = (SecurityContentUser) authentication.getPrincipal();

        RegistrationMetaData registrationMetaData =
                registrationService.getRegistrationMetaData(currentUser.getUserId());

        model.addAttribute("joinTime", registrationMetaData.getJoinDateTime());
        return "ucp/overview/statistics";
    }
}
