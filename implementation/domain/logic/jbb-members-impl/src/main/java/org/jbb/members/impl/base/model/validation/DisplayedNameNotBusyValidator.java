/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation;

import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class DisplayedNameNotBusyValidator implements ConstraintValidator<DisplayedNameNotBusy, DisplayedName> {
    @Autowired
    private MemberRepository memberRepository;

    @Override
    public void initialize(DisplayedNameNotBusy displayedNameNotBusy) {
        // not needed
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(DisplayedName displayedName, ConstraintValidatorContext constraintValidatorContext) {
        return memberRepository.countByDisplayedName(displayedName) == 0;
    }
}