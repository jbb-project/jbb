/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.signin.controller;

import org.jbb.lib.mvc.flow.RedirectManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SignInController {
    private final RedirectManager redirectManager;

    @Autowired
    public SignInController(RedirectManager redirectManager) {
        this.redirectManager = redirectManager;
    }

    private static String getErrorMessage(HttpServletRequest request, String key) {

        Exception exception = (Exception) request.getSession()
                .getAttribute(key);

        String error;
        if (exception instanceof BadCredentialsException) {
            error = "Invalid username or password";
        } else {
            error = "Some error occurred. Please contact administrator";
        }

        return error;
    }

    @RequestMapping(path = "/signin", method = RequestMethod.GET)
    public String signIn(@RequestParam(value = "error", required = false) Boolean error,
                         Model model, HttpServletRequest request, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            return redirectManager.goToPreviousPage(request);
        }

        if (error != null) {
            model.addAttribute("loginError", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
        }

        return "signin";

    }
}
