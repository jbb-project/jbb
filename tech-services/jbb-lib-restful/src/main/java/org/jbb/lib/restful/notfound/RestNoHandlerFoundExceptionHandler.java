/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.notfound;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.jbb.lib.mvc.PathResolver;
import org.jbb.lib.mvc.notfound.NoHandlerFoundExceptionHandler;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class RestNoHandlerFoundExceptionHandler implements NoHandlerFoundExceptionHandler {

    private final PathResolver pathResolver;
    private final ObjectMapper objectMapper;

    @Override
    public Optional<ModelAndView> handle(NoHandlerFoundException e) {
        if (pathResolver.isRequestToApi()) {
            ModelAndView mav = new ModelAndView(new MappingJackson2JsonView(objectMapper));
            ErrorResponse errorResponse = ErrorResponse.createFrom(ErrorInfo.NO_HANDLER_FOUND);
            mav.addObject(errorResponse);
            mav.setStatus(errorResponse.getStatus());
            return Optional.of(mav);
        }
        return Optional.empty();
    }
}
