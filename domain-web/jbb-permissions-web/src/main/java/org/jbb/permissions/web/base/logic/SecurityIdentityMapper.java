/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.web.base.logic;

import org.apache.commons.lang3.StringUtils;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.MemberService;
import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.identity.AnonymousIdentity;
import org.jbb.permissions.api.identity.MemberIdentity;
import org.jbb.permissions.api.identity.RegisteredMembersIdentity;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.web.base.form.SecurityIdentityChooseForm;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityIdentityMapper {

    public static final String REGISTERED_MEMBERS = "REGISTERED_MEMBERS";
    public static final String ANONYMOUS = "ANONYMOUS";
    public static final String ADMIN_GROUP = "ADMIN_GROUP";

    private final MemberService memberService;

    public Optional<SecurityIdentity> toModel(SecurityIdentityChooseForm form) {

        if (StringUtils.isNotBlank(form.getMemberDisplayedName())) {
            DisplayedName displayedName = DisplayedName.builder().value(StringUtils.defaultString(form.getMemberDisplayedName())).build();
            return memberService.getMemberWithDisplayedName(displayedName)
                    .map(member -> new MemberIdentity(member.getId()));
        }

        String identityType = StringUtils.defaultString(form.getIdentityType());

        switch (identityType) {
            case REGISTERED_MEMBERS:
                return Optional.of(RegisteredMembersIdentity.getInstance());
            case ANONYMOUS:
                return Optional.of(AnonymousIdentity.getInstance());
            case ADMIN_GROUP:
                return Optional.of(AdministratorGroupIdentity.getInstance());
            default:
                return Optional.empty();
        }
    }
}
