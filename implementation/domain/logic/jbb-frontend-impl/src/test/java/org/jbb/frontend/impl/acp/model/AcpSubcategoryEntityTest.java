/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp.model;

import com.google.common.collect.Lists;

import org.jbb.lib.test.BeanTesting;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AcpSubcategoryEntityTest extends BeanTesting<AcpSubcategoryEntity> {

    @Test
    public void builderTest() throws Exception {
        // when
        AcpSubcategoryEntity acpSubcategoryEntity = AcpSubcategoryEntity.builder()
                .name("foo")
                .ordering(1)
                .elements(Lists.newArrayList())
                .build();

        // then
        assertThat(acpSubcategoryEntity).isNotNull();
    }

}