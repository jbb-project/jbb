/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp.model;

import org.jbb.lib.test.BeanTesting;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UcpElementEntityTest extends BeanTesting<UcpElementEntity> {

    @Test
    public void builderTest() throws Exception {
        // when         .
        UcpElementEntity ucpElementEntity = UcpElementEntity.builder()
                .name("foo")
                .viewName("bar")
                .ordering(1)
                .build();

        // then
        assertThat(ucpElementEntity).isNotNull();
    }

}