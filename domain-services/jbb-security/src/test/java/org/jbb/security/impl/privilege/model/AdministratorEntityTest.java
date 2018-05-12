/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.privilege.model;

import static org.assertj.core.api.Assertions.assertThat;

import org.jbb.lib.test.PojoTest;
import org.junit.Test;

public class AdministratorEntityTest extends PojoTest {

    @Override
    public Class getClassUnderTest() {
        return AdministratorEntity.class;
    }

    @Test
    public void builderTest() throws Exception {
        // when
        AdministratorEntity administratorEntity = AdministratorEntity.builder()
                .memberId(133L)
                .build();

        // then
        assertThat(administratorEntity).isNotNull();
    }

}