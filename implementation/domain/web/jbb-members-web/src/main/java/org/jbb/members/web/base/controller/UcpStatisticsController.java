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

import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.RegistrationMetaData;
import org.jbb.members.api.service.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UcpStatisticsController {
    @Autowired
    private RegistrationService registrationService;

    @RequestMapping(value = "/ucp/overview/statistics")
    public String statistics(Model model, Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();

        RegistrationMetaData registrationMetaData = registrationService.getRegistrationMetaData(
                Username.builder().value(currentUser.getUsername()).build());

        model.addAttribute("joinTime", registrationMetaData.getJoinDateTime());
        return "ucp/overview/statistics";
    }
}
