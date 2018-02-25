/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.account;

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Password;
import org.jbb.members.api.base.AccountDataToChange;
import org.jbb.members.api.base.Member;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class AccountTranslator {

    public AccountDto toDto(Member member) {
        return AccountDto.builder()
                .email(member.getEmail().getValue())
                .build();
    }

    public AccountDataToChange toModel(UpdateAccountDto updateAccount) {
        String email = updateAccount.getEmail();
        String newPassword = updateAccount.getNewPassword();

        Optional<Email> emailOptional = email != null ?
                Optional.of(Email.builder().value(email).build()) :
                Optional.empty();

        Optional<Password> passwordOptional = newPassword != null ?
                Optional.of(Password.builder().value(newPassword.toCharArray()).build()) :
                Optional.empty();

        return AccountDataToChange.builder()
                .email(emailOptional)
                .newPassword(passwordOptional)
                .build();
    }

}
