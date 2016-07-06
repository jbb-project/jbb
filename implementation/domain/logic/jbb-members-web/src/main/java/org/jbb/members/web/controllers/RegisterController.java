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

import org.jbb.members.api.exceptions.RegistrationException;
import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.Email;
import org.jbb.members.api.model.Login;
import org.jbb.members.api.services.RegistrationService;
import org.jbb.members.web.form.DisplayedNameConverter;
import org.jbb.members.web.form.EmailConverter;
import org.jbb.members.web.form.LoginConverter;
import org.jbb.members.web.form.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class RegisterController {
    @Autowired
    private RegistrationService registrationService;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Login.class, new LoginConverter());
        binder.registerCustomEditor(DisplayedName.class, new DisplayedNameConverter());
        binder.registerCustomEditor(Email.class, new EmailConverter());
    }

    @RequestMapping("/register")
    public String signUp(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegisterForm(@ModelAttribute("registerForm") RegisterForm registerForm, BindingResult result) {
        try {
            registrationService.register(registerForm);
        } catch (RegistrationException e) {
            //FIXME reject values of BindingResult according to given errors (e.getErrors())
            return "register";
        }
        return "redirect:/";
    }

}
