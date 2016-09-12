/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.logic;

import org.jbb.lib.core.vo.Password;
import org.jbb.lib.core.vo.Username;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class PasswordEntityFactoryTest {
    @Mock
    private PasswordEncoder passwordEncoderMock;

    @InjectMocks
    private PasswordEntityFactory passwordEntityFactory;

    @Test
    public void shouldEncodePasswordBeforePersist_whenChange() throws Exception {
        // given
        Username username = Username.builder().value("john").build();
        Password password = Password.builder().value("myPassword1".toCharArray()).build();

        // when
        passwordEntityFactory.create(username, password);

        // then
        verify(passwordEncoderMock, times(1)).encode(eq("myPassword1"));
    }

}