/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.registration.model;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.test.PojoTest;
import org.junit.Test;

public class RegistrationMetaDataEntityTest extends PojoTest {

    @Override
    public Class getClassUnderTest() {
        return RegistrationMetaDataEntity.class;
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