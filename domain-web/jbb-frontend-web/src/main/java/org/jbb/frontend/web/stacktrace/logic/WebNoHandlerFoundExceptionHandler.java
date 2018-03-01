/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.stacktrace.logic;

import org.jbb.lib.mvc.notfound.NoHandlerFoundExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.Optional;

@Component
public class WebNoHandlerFoundExceptionHandler implements NoHandlerFoundExceptionHandler {

    private static final String NOT_FOUND_EXCEPTION_VIEW_NAME = "notFoundException";

    @Override
    public Optional<ModelAndView> handle(NoHandlerFoundException e) {
        if (!e.getRequestURL().startsWith("/api")) {
            return Optional.of(new ModelAndView(NOT_FOUND_EXCEPTION_VIEW_NAME));
        } else {
            return Optional.empty();
        }
    }
}
