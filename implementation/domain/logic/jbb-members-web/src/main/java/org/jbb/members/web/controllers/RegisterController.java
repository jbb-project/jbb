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

import org.apache.commons.lang3.StringUtils;
import org.jbb.members.api.exceptions.RegistrationException;
import org.jbb.members.api.model.RegistrationDetails;
import org.jbb.members.api.services.RegistrationService;
import org.jbb.members.web.form.RegisterForm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import java.util.Set;

@Controller
public class RegisterController {
    @Autowired
    private RegistrationService registrationService;

    @RequestMapping("/register")
    public String signUp(Model model) {
        model.addAttribute("registerForm", new RegisterForm());
        model.addAttribute("registrationCompleted", false);
        return "register";
    }

    @RequestMapping("/register/success")
    public String signUpSuccess(Model model) {
        model.addAttribute("registrationCompleted", true);
        return "register";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegisterForm(Model model,
                                      @ModelAttribute("registerForm") RegisterForm registerForm,
                                      BindingResult result, HttpServletRequest httpServletRequest) {
        try {
            RegistrationDetails registrationDetails = registerForm.registrationDetails().;
            RegistrationDetails.builder()
            registrationService.register();
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
            for (ConstraintViolation violation : constraintViolations) {
                result.rejectValue(unwrap(violation.getPropertyPath().toString()), "x", violation.getMessage());
            }
            model.addAttribute("registrationCompleted", false);
            return "register";
        }
        return "redirect:/register/success";
    }

    private String unwrap(String s) {
        return StringUtils.removeEndIgnoreCase(s, ".value");
    }

}
