/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.mvc.notfound;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;

@ControllerAdvice
@RequiredArgsConstructor
public class NoHandlerFoundControllerAdvice {

    private final List<NoHandlerFoundExceptionHandler> noHandlerFoundHandlers;

    @ExceptionHandler(value = {NoHandlerFoundException.class})
    public ModelAndView notFoundExceptionHandler(NoHandlerFoundException e) {
        for (NoHandlerFoundExceptionHandler handler : noHandlerFoundHandlers) {
            Optional<ModelAndView> response = handler.handle(e);
            if (response.isPresent()) {
                return response.get();
            }
        }
        throw new IllegalStateException("All NoHandlerFoundExceptionHandlers can't process exception", e);
    }

}
