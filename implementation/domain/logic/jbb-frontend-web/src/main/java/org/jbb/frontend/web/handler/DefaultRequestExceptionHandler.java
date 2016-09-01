/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.handler;

import org.jbb.frontend.web.interceptors.ReplacingViewInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class DefaultRequestExceptionHandler {

    private static final String DEFAULT_ERROR_VIEW_NAME = "error";

    @Autowired
    private ReplacingViewInterceptor replacingViewInterceptor;

    @ExceptionHandler(value = {RuntimeException.class, Exception.class, NoHandlerFoundException.class})
    public ModelAndView defaultErrorHandler(HttpServletResponse response, HttpServletRequest request, Exception e) throws Exception {
        ModelAndView modelAndView = new ModelAndView(DEFAULT_ERROR_VIEW_NAME);

        modelAndView.addObject("requestURL", request.getRequestURL());
        modelAndView.addObject("message", e.getMessage());
        modelAndView.addObject("status", response.getStatus());
        modelAndView.addObject("jbbVersion", request.getAttribute("jbbVersion"));
        modelAndView.addObject("boardName", request.getAttribute("boardName"));

        replacingViewInterceptor.postHandle(request, response, new Object(), modelAndView);

        return modelAndView;
    }
}
