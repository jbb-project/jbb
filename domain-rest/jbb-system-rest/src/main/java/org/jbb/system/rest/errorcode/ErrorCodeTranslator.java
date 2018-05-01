/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.errorcode;

import org.jbb.lib.restful.domain.ErrorInfo;
import org.springframework.stereotype.Component;

@Component
public class ErrorCodeTranslator {

    public ErrorCodeDto toDto(ErrorInfo errorInfo) {
        return ErrorCodeDto.builder()
                .errorName(errorInfo.name())
                .httpStatusName(errorInfo.getStatus().name())
                .httpStatus(errorInfo.getStatus().value())
                .errorCode(errorInfo.getCode())
                .errorMessage(errorInfo.getMessage())
                .build();
    }
}
