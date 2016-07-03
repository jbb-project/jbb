/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.form;

import org.apache.commons.lang3.StringUtils;
import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.Email;
import org.jbb.members.api.model.Login;

import javax.validation.Valid;

public class RegisterForm {
    @Valid
    private Login login = Login.builder().value(StringUtils.EMPTY).build();

    @Valid
    private DisplayedName displayedName = DisplayedName.builder().value(StringUtils.EMPTY).build();

    @Valid
    private Email email = Email.builder().value(StringUtils.EMPTY).build();

    public Login getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = Login.builder().value(login).build();
    }

    public DisplayedName getDisplayedName() {
        return displayedName;
    }

    public void setDisplayedName(String displayedName) {
        this.displayedName = DisplayedName.builder().value(displayedName).build();
    }

    public Email getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = Email.builder().value(email).build();
    }
}
