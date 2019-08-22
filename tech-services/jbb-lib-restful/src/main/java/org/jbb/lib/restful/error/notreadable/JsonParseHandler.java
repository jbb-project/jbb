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

import com.fasterxml.jackson.core.JsonParseException;

import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorResponse;
import org.springframework.stereotype.Component;

@Component
public class JsonParseHandler implements NotReadableExceptionHandler<JsonParseException> {

    @Override
    public Class<JsonParseException> getSupportedClass() {
        return JsonParseException.class;
    }

    @Override
    public ErrorResponse handle(JsonParseException ex) {
        return ErrorResponse.createFrom(ErrorInfo.JSON_PARSING_ERROR);
    }
}
