/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.commons;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Test;

public class RequestIdUtilsTest {

    @Test
    public void requestIdGenerationTest() throws Exception {
        String newRequestId = RequestIdUtils.generateNewRequestId();

        assertThat(RequestIdUtils.getCurrentRequestId()).isEqualTo(newRequestId);

        RequestIdUtils.cleanRequestId();
        assertThat(RequestIdUtils.getCurrentRequestId()).isNull();
    }
}