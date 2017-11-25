/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation.create;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UsernameNotBusyCreateValidator implements
    ConstraintValidator<UsernameNotBusyCreate, Username> {

    private final MemberRepository memberRepository;

    @Override
    public void initialize(UsernameNotBusyCreate usernameNotBusy) {
        // not needed...
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(Username username,
        ConstraintValidatorContext constraintValidatorContext) {
        return memberRepository.countByUsername(username) == 0;
    }

}
