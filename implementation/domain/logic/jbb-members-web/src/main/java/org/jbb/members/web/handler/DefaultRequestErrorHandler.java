/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.handler;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class DefaultRequestErrorHandler {

    private final static String DEFAULT_ERROR_VIEW_NAME = "error";

    @ExceptionHandler(value = {RuntimeException.class, Exception.class})
    public ModelAndView defaultErrorHandler(HttpServletRequest request, Exception e) {

        ModelAndView modelAndView = new ModelAndView(DEFAULT_ERROR_VIEW_NAME);
        modelAndView.addObject("requestURL", request.getRequestURL());
        modelAndView.addObject("errorMessage", e.getMessage());


        return modelAndView;
    }
}
