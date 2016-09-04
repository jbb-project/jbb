/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.model;

import org.jbb.lib.core.vo.Login;
import org.jbb.security.impl.role.model.AdministratorEntity;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import static org.assertj.core.api.Assertions.assertThat;

public class AdministratorEntityTest {

    @Test
    public void pojoTest() throws Exception {
        BeanTester beanTester = new BeanTester();
        beanTester.setIterations(3);

        beanTester.testBean(AdministratorEntity.class);
    }

    @Test
    public void builderTest() throws Exception {
        // when
        AdministratorEntity administratorEntity = AdministratorEntity.builder()
                .login(Login.builder().value("john").build())
                .build();

        // then
        assertThat(administratorEntity).isNotNull();
    }

}