/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.swagger;

import static java.util.Collections.emptySet;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toSet;

import com.google.common.base.Optional;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import springfox.documentation.builders.ResponseMessageBuilder;
import springfox.documentation.service.ResponseMessage;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.OperationBuilderPlugin;
import springfox.documentation.spi.service.contexts.OperationContext;

@Component
public class ErrorInfoCodesSwaggerPlugin implements OperationBuilderPlugin {

    @Override
    public void apply(OperationContext operationContext) {
        operationContext.operationBuilder()
            .responseMessages(fetchResponseMessages(operationContext));
    }

    private Set<ResponseMessage> fetchResponseMessages(OperationContext context) {
        Optional<ErrorInfoCodes> errorInfoCodesAnnotation = context
            .findAnnotation(ErrorInfoCodes.class);
        if (errorInfoCodesAnnotation.isPresent()) {
            Map<HttpStatus, List<ErrorInfo>> errorsByHttpStatus = Arrays
                .stream(errorInfoCodesAnnotation.get().value())
                .collect(groupingBy(ErrorInfo::getStatus));

            return errorsByHttpStatus.entrySet().stream()
                .map(errorsSet -> {
                    HttpStatus httpStatus = errorsSet.getKey();
                    List<ErrorInfo> errors = errorsSet.getValue();
                    String message = ErrorInfo.joinedMessages(errors);

                    return new ResponseMessageBuilder()
                        .code(httpStatus.value())
                        .message(message)
                        .build();
                })
                .collect(toSet());
        }
        return emptySet();
    }

    @Override
    public boolean supports(DocumentationType documentationType) {
        return true;
    }

}
