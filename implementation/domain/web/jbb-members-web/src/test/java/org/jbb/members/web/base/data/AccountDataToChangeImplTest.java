/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.data;

import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.Password;
import org.junit.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class AccountDataToChangeImplTest {
    private AccountDataToChangeImpl accountDataToChange = new AccountDataToChangeImpl();

    @Test
    public void shouldGetEmail_afterSet() throws Exception {
        // given
        Email expectedEmail = Email.builder().value("foo@bar.com").build();

        // when
        accountDataToChange.setEmail(expectedEmail);
        Optional<Email> email = accountDataToChange.getEmail();

        // then
        assertThat(email.get()).isEqualTo(expectedEmail);
    }

    @Test
    public void shouldGetPassword_afterSet() throws Exception {
        // given
        Password expectedPassword = Password.builder().value("pass".toCharArray()).build();

        // when
        accountDataToChange.setNewPassword(expectedPassword);
        Optional<Password> password = accountDataToChange.getNewPassword();

        // then
        assertThat(password.get()).isEqualTo(expectedPassword);
    }
}