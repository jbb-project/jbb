/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.interceptors;


import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import java.time.Clock;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@Order(1)
@RequiredArgsConstructor
public class RequestTimeInterceptor extends HandlerInterceptorAdapter {

    static final String REQUEST_START_TIME_ATTRIBUTE = "interceptor_request_start_time";

    private final Clock clock;

    public RequestTimeInterceptor() {
        this.clock = Clock.systemDefaultZone();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        long startTime = clock.millis();
        request.setAttribute(REQUEST_START_TIME_ATTRIBUTE, startTime);

        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {

        long startTime = (long) request.getAttribute(REQUEST_START_TIME_ATTRIBUTE);
        long currentTime = clock.millis();
        long requestTime = currentTime - startTime;

        log.debug("Request URL: {} , execution time : {} milliseconds", request.getRequestURL(), requestTime);

    }
}
