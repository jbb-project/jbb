/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.entities.validation;

import org.jbb.lib.core.vo.Login;
import org.jbb.members.dao.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class LoginNotBusyValidator implements ConstraintValidator<LoginNotBusy, Login> {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void initialize(LoginNotBusy loginNotBusy) {
        // not needed
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(Login login, ConstraintValidatorContext constraintValidatorContext) {
        return memberRepository.countByLogin(login) == 0;
    }
}
