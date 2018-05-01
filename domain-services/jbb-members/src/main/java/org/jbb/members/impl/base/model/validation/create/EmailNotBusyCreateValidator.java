/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation.create;

import org.jbb.lib.commons.vo.Email;
import org.jbb.members.impl.base.MembersProperties;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EmailNotBusyCreateValidator implements ConstraintValidator<EmailNotBusyCreate, Email> {

    private final MemberRepository memberRepository;
    private final MembersProperties properties;

    @Override
    public void initialize(EmailNotBusyCreate emailNotBusy) {
        // not needed...
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(Email email, ConstraintValidatorContext constraintValidatorContext) {
        return properties.allowEmailDuplication()
                || memberRepository.countByEmail(email) == 0;
    }

}
