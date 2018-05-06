/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.web.base.logic.error;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.mvc.PathResolver;
import org.jbb.lib.mvc.notfound.NoHandlerFoundExceptionHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

@Component
@RequiredArgsConstructor
public class WebNoHandlerFoundExceptionHandler implements NoHandlerFoundExceptionHandler {

    private static final String NOT_FOUND_EXCEPTION_VIEW_NAME = "notFoundException";

    private final PathResolver pathResolver;

    @Override
    public Optional<ModelAndView> handle(NoHandlerFoundException e) {
        if (pathResolver.isRequestToApi()) {
            return Optional.empty();
        } else {
            return Optional.of(new ModelAndView(NOT_FOUND_EXCEPTION_VIEW_NAME));
        }
    }
}
