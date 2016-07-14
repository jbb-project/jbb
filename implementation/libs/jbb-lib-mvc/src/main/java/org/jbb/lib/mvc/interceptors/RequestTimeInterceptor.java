/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.interceptors;


import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RequestTimeInterceptor extends HandlerInterceptorAdapter {

    private final static String REQUEST_START_TIME_ATTRIBUTE = "interceptor_request_start_time";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long startTime = System.currentTimeMillis();
        request.setAttribute(REQUEST_START_TIME_ATTRIBUTE, startTime);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

        long startTime = (long) request.getAttribute(REQUEST_START_TIME_ATTRIBUTE);
        long currentTime = System.currentTimeMillis();
        long requestTime = currentTime - startTime;

        log.info("Request URL {} , execution time : {} milliseconds", request.getRequestURL(), requestTime);

    }
}
