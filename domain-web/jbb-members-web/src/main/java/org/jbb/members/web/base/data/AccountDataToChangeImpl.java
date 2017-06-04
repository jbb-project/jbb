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

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Password;
import org.jbb.members.api.base.AccountDataToChange;

import java.util.Optional;

public class AccountDataToChangeImpl implements AccountDataToChange {
    private Optional<Email> email = Optional.empty();
    private Optional<Password> newPassword = Optional.empty();

    @Override
    public Optional<Email> getEmail() {
        return email;
    }

    public void setEmail(Email email) {
        this.email = Optional.of(email);
    }

    @Override
    public Optional<Password> getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(Password newPassword) {
        this.newPassword = Optional.of(newPassword);
    }
}
