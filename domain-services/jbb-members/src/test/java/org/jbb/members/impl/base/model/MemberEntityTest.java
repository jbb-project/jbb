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

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.test.PojoTest;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.impl.registration.model.RegistrationMetaDataEntity;
import org.junit.Test;

public class MemberEntityTest extends PojoTest {

    @Override
    public Class getClassUnderTest() {
        return MemberEntity.class;
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