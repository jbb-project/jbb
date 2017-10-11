/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.signin.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.mvc.flow.RedirectManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.LockedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class SignInController {
    private static final String BAD_CREDENTIALS_MESSAGE = "Invalid username or password";
    private static final String LOCKING_MESSAGE = "Your account has been temporary locked due to many invalid sign in attempts. " +
            "Please try again later or contact administrator";
    private static final String GENERIC_MESSAGE = "Some error occurred. Please contact administrator";


    private final RedirectManager redirectManager;


    private String getErrorMessage(HttpServletRequest request, String key) {

        Exception exception = (Exception) request.getSession()
                .getAttribute(key);

        String error;
        if (exception instanceof BadCredentialsException) {
            error = BAD_CREDENTIALS_MESSAGE;
        } else if (exception instanceof LockedException) {
            error = LOCKING_MESSAGE;
        } else {
            error = GENERIC_MESSAGE;
        }

        return error;
    }

    @RequestMapping(path = "/signin", method = RequestMethod.GET)
    public String signIn(@RequestParam(value = "error", required = false) Boolean error,
                         Model model, HttpServletRequest request, Authentication authentication,
                         HttpSession session) {
        if (authentication != null && authentication.isAuthenticated()) {
            return redirectManager.goToPreviousPageSafe(request);
        } else {
            session.setAttribute("pageBeforeSignIn", request.getHeader("referer"));
        }

        if (error != null) {
            model.addAttribute("loginError", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
        }

        return "signin";

    }
}
