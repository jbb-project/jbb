/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model.validation.update;

import java.util.List;
import java.util.Optional;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.impl.base.MembersProperties;
import org.jbb.members.impl.base.dao.MemberRepository;
import org.jbb.members.impl.base.model.MemberEntity;
import org.jbb.security.api.role.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

public class EmailNotBusyUpdateValidator implements
    ConstraintValidator<EmailNotBusyUpdate, MemberEntity> {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private UserDetailsSource userDetailsSource;

    @Autowired
    private MembersProperties properties;

    @Autowired
    private RoleService roleService;

    @Override
    public void initialize(EmailNotBusyUpdate emailNotBusy) {
        // not needed...
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isValid(MemberEntity memberEntity, ConstraintValidatorContext context) {
        Long memberId = memberEntity.getId();
        Email email = memberEntity.getEmail();
        List<MemberEntity> membersWithEmail = memberRepository.findByEmail(email);

        boolean result = properties.allowEmailDuplication()
            || membersWithEmail.isEmpty()
            || currentUserIsUsing(email)
            || (callerIsAnAdministrator() && editsProperMember(memberEntity, memberId));

        if (!result) {
            context.disableDefaultConstraintViolation();
            context
                .buildConstraintViolationWithTemplate(context.getDefaultConstraintMessageTemplate())
                    .addPropertyNode("email").addConstraintViolation();
        }
        return result;
    }

    private boolean editsProperMember(MemberEntity memberEntity, Long memberId) {
        return memberEntity.getId().equals(memberId);
    }

    private boolean currentUserIsUsing(Email email) {
        SecurityContentUser currentUser = userDetailsSource.getFromApplicationContext();

        if (currentUser != null) {
            Username currentUsername = Username.builder().value(currentUser.getUsername()).build();
            Optional<MemberEntity> currentMember = memberRepository.findByUsername(currentUsername);
            return currentMember.isPresent() && currentMember.get().getEmail().equals(email);
        }
        return false;
    }

    private boolean callerIsAnAdministrator() {
        SecurityContentUser userDetails = userDetailsSource.getFromApplicationContext();
        return userDetails != null && roleService.hasAdministratorRole(userDetails.getUserId());
    }
}
