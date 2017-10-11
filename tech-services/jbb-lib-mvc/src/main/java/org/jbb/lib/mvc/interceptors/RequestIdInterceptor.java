/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.interceptors;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.commons.RequestIdUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Slf4j
@Component
@Order(2)
public class RequestIdInterceptor extends HandlerInterceptorAdapter {

    static final String REQUEST_ID_HEADER_NAME = "request-id";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response,
        Object handler) throws Exception {
        String currentRequestId = RequestIdUtils.getCurrentRequestId();
        if (StringUtils.isNotBlank(currentRequestId)) {
            response.setHeader(REQUEST_ID_HEADER_NAME, currentRequestId);
        }
        return true;
    }

}
