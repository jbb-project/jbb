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

import org.jbb.lib.core.security.UserDetailsSource;
import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.Member;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;


public class DisplayedNameNotBusyValidator implements ConstraintValidator<DisplayedNameNotBusy, DisplayedName> {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserDetailsSource userDetailsSource;

    @Override
    public void initialize(DisplayedNameNotBusy displayedNameNotBusy) {
        // not needed
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(DisplayedName displayedName, ConstraintValidatorContext constraintValidatorContext) {
        Long counter = memberRepository.countByDisplayedName(displayedName);
        return counter == 0 || (counter == 1 && currentUserIsUsing(displayedName));
    }

    private boolean currentUserIsUsing(DisplayedName displayedName) {
        UserDetails userDetails = userDetailsSource.getFromApplicationContext();
        if (userDetails != null) {
            Username currentUsername = Username.builder().value(userDetails.getUsername()).build();
            Member memberWithDisplayedName = memberRepository.findByDisplayedName(displayedName);
            return memberWithDisplayedName.getUsername().equals(currentUsername);
        }
        return false;
    }
}
