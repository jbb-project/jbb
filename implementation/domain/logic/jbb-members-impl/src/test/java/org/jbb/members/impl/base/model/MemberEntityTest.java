/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model;

import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.IPAddress;
import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.impl.registration.model.RegistrationMetaDataEntity;
import org.junit.Test;
import org.meanbean.test.BeanTester;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberEntityTest {
    @Test
    public void pojoTest() throws Exception {
        BeanTester beanTester = new BeanTester();
        beanTester.setIterations(3);
        beanTester.getFactoryCollection().addFactory(LocalDateTime.class, () -> LocalDateTime.now());

        beanTester.testBean(MemberEntity.class);
    }

    @Test
    public void builderTest() throws Exception {
        // when
        MemberEntity memberEntity = MemberEntity.builder()
                .username(Username.builder().value("john").build())
                .displayedName(DisplayedName.builder().value("John").build())
                .email(Email.builder().value("john@john.com").build())
                .registrationMetaData(RegistrationMetaDataEntity.builder()
                        .ipAddress(IPAddress.builder().value("127.0.0.1").build())
                        .joinDateTime(LocalDateTime.now()).build())
                .build();

        // then
        assertThat(memberEntity).isNotNull();
    }
}