/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.userdetails.data;


import org.jbb.lib.core.security.SecurityContentUser;
import org.junit.Test;
import org.springframework.security.core.userdetails.User;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

public class SecurityContentUserTest {
    @Test
    public void shouldGetDisplayedNameAndUserId() throws Exception {
        // given
        User userMock = mock(User.class);
        given(userMock.getUsername()).willReturn("john");
        given(userMock.getPassword()).willReturn("p@ss1");

        // when
        SecurityContentUser securityContentUser = new SecurityContentUser(userMock, "John", 123L);
        String displayedName = securityContentUser.getDisplayedName();
        Long userId = securityContentUser.getUserId();

        // then
        assertThat(displayedName).isEqualTo("John");
        assertThat(userId).isEqualTo(123L);
    }
}