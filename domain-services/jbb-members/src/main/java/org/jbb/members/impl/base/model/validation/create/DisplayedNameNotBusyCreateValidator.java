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

import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DisplayedNameNotBusyCreateValidator implements
        ConstraintValidator<DisplayedNameNotBusyCreate, DisplayedName> {

    private final MemberRepository memberRepository;

    @Override
    public void initialize(DisplayedNameNotBusyCreate displayedNameNotBusy) {
        // not needed...
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(DisplayedName displayedName,
                           ConstraintValidatorContext constraintValidatorContext) {
        return memberRepository.countByDisplayedName(displayedName) == 0;
    }

}
