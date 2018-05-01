/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.registration.controller;

import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.members.api.registration.RegistrationException;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.web.registration.form.RegisterForm;
import org.jbb.members.web.registration.logic.RegistrationErrorsBindingMapper;
import org.jbb.members.web.registration.logic.RegistrationRequestBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/register")
public class RegisterController {
    private static final String REGISTER_FORM = "registerForm";
    private static final String REGISTER_COMPLETE = "registrationCompleted";
    private static final String REGISTER_VIEW_NAME = "register";
    private static final String NEW_MEMBER_USERNAME = "newMemberUsername";

    private final RegistrationService registrationService;
    private final RegistrationErrorsBindingMapper errorsBindingMapper;
    private final RegistrationRequestBuilder registrationRequestBuilder;

    @RequestMapping(method = RequestMethod.GET)
    public String signUp(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return "redirect:/";
        }
        log.debug("Open fresh registration form");
        model.addAttribute(REGISTER_FORM, new RegisterForm());
        model.addAttribute(REGISTER_COMPLETE, false);
        return REGISTER_VIEW_NAME;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String processRegisterForm(Model model,
                                      @ModelAttribute(REGISTER_FORM) RegisterForm registerForm,
                                      BindingResult result, HttpServletRequest httpServletRequest,
                                      RedirectAttributes redirectAttributes) {
        IPAddress ipAddress = IPAddress.builder().value(httpServletRequest.getRemoteAddr()).build();
        RegistrationRequest registrationRequest = registrationRequestBuilder
                .buildRequest(registerForm, ipAddress);

        try {
            registrationService.register(registrationRequest);
        } catch (RegistrationException e) {
            Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
            log.debug("Validation error of user input data during registration: {}", violations, e);
            errorsBindingMapper.map(violations, result);
            model.addAttribute(REGISTER_COMPLETE, false);
            return REGISTER_VIEW_NAME;
        }
        redirectAttributes.addFlashAttribute(NEW_MEMBER_USERNAME, registerForm.getUsername());
        model.addAttribute(REGISTER_COMPLETE, true);
        return "redirect:/register/success";
    }

    @RequestMapping(value = "/success", method = RequestMethod.GET)
    public String signUpSuccess(Model model) {
        String newMemberUsername = (String) model.asMap().get(NEW_MEMBER_USERNAME);
        if (newMemberUsername == null) {
            log.warn("Invoked /register/success not through redirection from registration form");
            return "redirect:/register";
        } else {
            log.debug("Registration for member with username '{}' completed", newMemberUsername);
            model.addAttribute(REGISTER_COMPLETE, true);
            return REGISTER_VIEW_NAME;
        }
    }

}
