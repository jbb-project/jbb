/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful;

import com.google.common.collect.Lists;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jbb.lib.commons.RequestIdUtils;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    private HttpStatus status;
    private String code;
    private String message;
    private String requestId;

    @Builder.Default
    private List<ErrorDetail> details = Lists.newArrayList();

    public static ErrorResponse createFrom(ErrorInfo errorInfo) {
        return ErrorResponse.builder()
            .status(errorInfo.getStatus())
            .code(errorInfo.getCode())
            .message(errorInfo.getMessage())
            .requestId(RequestIdUtils.getCurrentRequestId())
            .build();
    }


}
