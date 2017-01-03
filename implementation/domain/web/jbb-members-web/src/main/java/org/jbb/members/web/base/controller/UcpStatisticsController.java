/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.controller;

import org.jbb.lib.core.security.SecurityContentUser;
import org.jbb.members.api.data.RegistrationMetaData;
import org.jbb.members.api.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UcpStatisticsController {
    private final RegistrationService registrationService;

    @Autowired
    public UcpStatisticsController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @RequestMapping(value = "/ucp/overview/statistics")
    public String statistics(Model model, Authentication authentication) {
        SecurityContentUser currentUser = (SecurityContentUser) authentication.getPrincipal();

        RegistrationMetaData registrationMetaData =
                registrationService.getRegistrationMetaData(currentUser.getUserId());

        model.addAttribute("joinTime", registrationMetaData.getJoinDateTime());
        return "ucp/overview/statistics";
    }
}
