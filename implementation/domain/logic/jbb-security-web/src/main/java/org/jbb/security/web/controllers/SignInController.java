/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.controllers;

import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

@Controller
public class SignInController {
    private static String getErrorMessage(HttpServletRequest request, String key) {

        Exception exception = (Exception) request.getSession()
                .getAttribute(key);

        String error;
        if (exception instanceof BadCredentialsException) {
            error = "Invalid username or password";
        } else {
            error = "Some error occured. Please contact administrator";
        }

        return error;
    }

    @RequestMapping(path = "/signin", method = RequestMethod.GET)
    public String signIn(@RequestParam(value = "error", required = false) Boolean error,
                         Model model, HttpServletRequest request) {

        if (error != null) {
            model.addAttribute("loginError", getErrorMessage(request, "SPRING_SECURITY_LAST_EXCEPTION"));
        }

        return "signin";

    }
}
