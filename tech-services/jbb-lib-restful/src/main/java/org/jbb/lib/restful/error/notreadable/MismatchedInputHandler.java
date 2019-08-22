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

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorDetail;
import org.jbb.lib.restful.error.ErrorResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MismatchedInputHandler implements NotReadableExceptionHandler<MismatchedInputException> {

    @Override
    public Class<MismatchedInputException> getSupportedClass() {
        return MismatchedInputException.class;
    }

    @Override
    public ErrorResponse handle(MismatchedInputException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(ErrorInfo.TYPE_MISMATCH_PROPERTY);
        List<JsonMappingException.Reference> path = ex.getPath();
        if (!path.isEmpty()) {
            String fieldName = path.get(0).getFieldName();
            errorResponse.getDetails().add(new ErrorDetail("property", fieldName));
        }
        return errorResponse;
    }

}
