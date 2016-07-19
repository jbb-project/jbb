/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.entities;

import org.jbb.lib.core.vo.IPAddress;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class RegistrationMetaDataEntityTest {
    @Test
    public void pojoTest() throws Exception {
        BeanTester beanTester = new BeanTester();
        beanTester.setIterations(3);
        beanTester.getFactoryCollection().addFactory(LocalDateTime.class, () -> LocalDateTime.now());

        beanTester.testBean(RegistrationMetaDataEntity.class);
    }

    @Test
    public void builderTest() throws Exception {
        // when
        RegistrationMetaDataEntity registrationEntity = RegistrationMetaDataEntity.builder()
                .ipAddress(IPAddress.builder().value("127.0.0.1").build())
                .joinDateTime(LocalDateTime.now()).build();

        // then
        assertThat(registrationEntity).isNotNull();
    }
}