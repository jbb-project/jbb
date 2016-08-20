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

import org.jbb.lib.core.vo.Email;
import org.jbb.members.dao.MemberRepository;
import org.jbb.members.properties.MembersProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class EmailNotBusyValidator implements ConstraintValidator<EmailNotBusy, Email> {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MembersProperties properties;


    @Override
    public void initialize(EmailNotBusy emailNotBusy) {
        // not needed
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(Email email, ConstraintValidatorContext constraintValidatorContext) {
        return properties.allowEmailDuplication() || memberRepository.countByEmail(email) == 0;
    }
}
