/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.e2e.serenity.rest;

import java.util.List;
import lombok.Data;

@Data
public class Page<T> {

    private List<T> content;
    private Boolean last;
    private Long totalPages;
    private Long totalElements;
    private Long size;
    private Long number;
    private Object sort;
    private Boolean first;
    private Long numberOfElements;

}
