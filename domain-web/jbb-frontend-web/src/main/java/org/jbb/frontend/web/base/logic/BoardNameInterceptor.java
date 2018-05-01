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

import org.jbb.board.api.base.BoardSettingsService;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.RequiredArgsConstructor;

@Component
@Order(3)
@RequiredArgsConstructor
public class BoardNameInterceptor extends HandlerInterceptorAdapter {

    private final BoardSettingsService boardSettingsService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        request.setAttribute("boardName", boardSettingsService.getBoardSettings().getBoardName());
        return true;
    }
}
