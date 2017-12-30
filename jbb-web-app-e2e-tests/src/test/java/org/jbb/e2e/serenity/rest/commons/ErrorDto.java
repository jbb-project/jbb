/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest.commons;

import com.google.common.collect.Lists;

import java.util.List;

import lombok.Data;

@Data
public class ErrorDto {

    private String status;
    private String code;
    private String message;
    private String requestId;

    private List<ErrorDetailDto> details = Lists.newArrayList();

}
