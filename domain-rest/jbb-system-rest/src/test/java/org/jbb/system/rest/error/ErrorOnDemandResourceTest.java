/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.system.rest.error;

import org.junit.Test;

public class ErrorOnDemandResourceTest {

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowRuntimeException() {
        // when
        new ErrorOnDemandResource().getError();

        // then
        // throw IllegalArgumentException
    }
}