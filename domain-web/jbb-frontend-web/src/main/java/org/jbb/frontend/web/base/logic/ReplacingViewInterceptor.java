/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jbb.frontend.web.base.logic.view.ReplacingViewStrategy;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
@Order(4)
@RequiredArgsConstructor
public class ReplacingViewInterceptor extends HandlerInterceptorAdapter {
    private final List<ReplacingViewStrategy> replacingStrategies;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) {

        for (ReplacingViewStrategy replacingStrategy : replacingStrategies) {
            if (replacingStrategy.handle(modelAndView)) {
                return;
            }
        }

    }
}
