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
import org.jbb.lib.core.vo.IPAddress;
import org.jbb.members.api.exceptions.RegistrationException;
import org.jbb.members.api.services.RegistrationService;
import org.jbb.members.web.form.RegisterForm;
import org.jbb.members.web.model.RegistrationRequestImpl;
import org.springframework.beans.factory.annotation.Autowired;
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

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class RegisterController {
    private static final String REGISTER_FORM = "registerForm";
    private static final String REGISTER_COMPLETE = "registrationCompleted";
    private static final String REGISTER_VIEW_NAME = "register";
    private static final String NEW_MEMBER_LOGIN = "newMemberLogin";

    @Autowired
    private RegistrationService registrationService;

    private static String unwrap(String s) {
        if ("visiblePassword".equals(s)) {
            return "password";//TODO
        }
        return StringUtils.removeEndIgnoreCase(s, ".value");
    }

    @RequestMapping("/register")
    public String signUp(Model model) {
        log.debug("Open fresh registration form");
        model.addAttribute(REGISTER_FORM, new RegisterForm());
        model.addAttribute(REGISTER_COMPLETE, false);
        return REGISTER_VIEW_NAME;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegisterForm(Model model,
                                      @ModelAttribute(REGISTER_FORM) RegisterForm registerForm,
                                      BindingResult result, HttpServletRequest httpServletRequest,
                                      RedirectAttributes redirectAttributes) {
        try {
            registrationService.register(
                    new RegistrationRequestImpl(registerForm, IPAddress.builder().value(httpServletRequest.getRemoteAddr()).build()));
        } catch (RegistrationException e) {
            log.debug("Validation error of user input data during registration: {}", e.getConstraintViolations(), e);
            Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
            for (ConstraintViolation violation : constraintViolations) {
                result.rejectValue(unwrap(violation.getPropertyPath().toString()), "x", violation.getMessage());
            }
            model.addAttribute(REGISTER_COMPLETE, false);
            return REGISTER_VIEW_NAME;
        }
        redirectAttributes.addFlashAttribute(NEW_MEMBER_LOGIN, registerForm.getLogin());
        return "redirect:/register/success";
    }

    @RequestMapping("/register/success")
    public String signUpSuccess(Model model) {
        String newMemberLogin = (String) model.asMap().get(NEW_MEMBER_LOGIN);
        if (newMemberLogin == null) {
            log.warn("Invoked /register/success not through redirection from registration form");
            return "redirect:/register";
        } else {
            log.debug("Registration for member with login '{}' completed", newMemberLogin);
            model.addAttribute(REGISTER_COMPLETE, true);
            return REGISTER_VIEW_NAME;
        }
    }

}
