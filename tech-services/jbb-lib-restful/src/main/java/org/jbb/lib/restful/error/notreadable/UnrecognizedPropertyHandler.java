/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.error.notreadable;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;

import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorDetail;
import org.jbb.lib.restful.error.ErrorResponse;
import org.springframework.stereotype.Component;

@Component
public class UnrecognizedPropertyHandler implements NotReadableExceptionHandler<UnrecognizedPropertyException> {

    @Override
    public Class<UnrecognizedPropertyException> getSupportedClass() {
        return UnrecognizedPropertyException.class;
    }

    @Override
    public ErrorResponse handle(UnrecognizedPropertyException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(ErrorInfo.UNRECOGNIZED_PROPERTY);
        errorResponse.getDetails().add(new ErrorDetail("property", ex.getPropertyName()));
        return errorResponse;
    }
}
