/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.controllers;

import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.Email;
import org.jbb.members.api.model.Login;
import org.jbb.members.api.model.RegistrationDetails;
import org.jbb.members.api.services.RegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class RegisterController {
    @Autowired
    private RegistrationService registrationService;

    @RequestMapping("/register")
    public String signUp() {
        registrationService.register(new RegistrationDetails() {//FIXME
            @Override
            public Login getLogin() {
                return Login.builder().value("log").build();
            }

            @Override
            public DisplayedName getDisplayedName() {
                return DisplayedName.builder().value("dssds").build();
            }

            @Override
            public Email getEmail() {
                return Email.builder().value("sdsd@dsds").build();
            }
        });
        return "register";
    }
}
