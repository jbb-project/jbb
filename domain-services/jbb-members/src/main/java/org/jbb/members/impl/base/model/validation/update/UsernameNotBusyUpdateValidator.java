/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation.update;

import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.privilege.PrivilegeService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
public class UsernameNotBusyUpdateValidator implements
        ConstraintValidator<UsernameNotBusyUpdate, MemberEntity> {

    private final MemberRepository memberRepository;
    private final UserDetailsSource userDetailsSource;
    private final PrivilegeService privilegeService;

    private String message;

    @Override
    public void initialize(UsernameNotBusyUpdate usernameNotBusy) {
        message = usernameNotBusy.message();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(MemberEntity memberEntity, ConstraintValidatorContext context) {
        Long memberId = memberEntity.getId();
        Username username = memberEntity.getUsername();
        Optional<MemberEntity> memberWithUsername = memberRepository.findByUsername(username);

        boolean result = !memberWithUsername.isPresent()
            || editsProperMember(memberWithUsername.get(), memberId) && (
                currentUserIsUsing(username) || callerIsAnAdministrator()
        );

        if (!result) {
            context.disableDefaultConstraintViolation();
            context
                    .buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("username").addConstraintViolation();
        }
        return result;
    }

    private boolean editsProperMember(MemberEntity memberEntity, Long memberId) {
        return memberEntity.getId().equals(memberId);
    }

    private boolean currentUserIsUsing(Username username) {
        UserDetails userDetails = userDetailsSource.getFromApplicationContext();
        return userDetails != null && userDetails.getUsername().equals(username.getValue());
    }

    private boolean callerIsAnAdministrator() {
        SecurityContentUser userDetails = userDetailsSource.getFromApplicationContext();
        return userDetails != null && privilegeService
            .hasAdministratorPrivilege(userDetails.getUserId());
    }
}
