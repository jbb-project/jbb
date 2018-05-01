/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.restful.error;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.test.PojoTest;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ErrorResponseTest extends PojoTest {

    @Override
    public Class getClassUnderTest() {
        return ErrorResponse.class;
    }

    @Test
    public void shouldSetResponseEntity() {
        // when
        ResponseEntity<ErrorResponse> responseEntity = ErrorResponse.getErrorResponseEntity(ErrorInfo.INTERNAL_ERROR);

        // when
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}