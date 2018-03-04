/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.acl;

import org.apache.commons.lang3.EnumUtils;
import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.identity.AnonymousIdentity;
import org.jbb.permissions.api.identity.MemberIdentity;
import org.jbb.permissions.api.identity.RegisteredMembersIdentity;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.identity.SecurityIdentity.Type;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityRepository;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityTypeRepository;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityEntity;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityTypeEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityIdentityTranslator {

    private final AclSecurityIdentityTypeRepository securityIdentityTypeRepository;
    private final AclSecurityIdentityRepository aclSecurityIdentityRepository;

    public AclSecurityIdentityEntity toNewEntity(SecurityIdentity securityIdentity) {
        AclSecurityIdentityTypeEntity identityType = securityIdentityTypeRepository
                .findAllByName(securityIdentity.getType().name());
        return AclSecurityIdentityEntity.builder()
                .type(identityType)
                .primarySid(securityIdentity.getId())
                .build();
    }

    public Optional<AclSecurityIdentityEntity> toEntity(SecurityIdentity securityIdentity) {
        AclSecurityIdentityTypeEntity identityType = securityIdentityTypeRepository
                .findAllByName(securityIdentity.getType().name());
        if (securityIdentity.getType() == Type.MEMBER) {
            return aclSecurityIdentityRepository
                    .findByTypeAndPrimarySid(identityType, securityIdentity.getId());
        } else {
            return Optional.ofNullable(aclSecurityIdentityRepository.findTopByType(identityType));
        }
    }

    public SecurityIdentity toApiModel(AclSecurityIdentityEntity identity) {
        String typeName = identity.getType().getName();
        Type securityIdentityType = EnumUtils.getEnum(Type.class, typeName);
        switch (securityIdentityType) {
            case ANONYMOUS:
                return AnonymousIdentity.getInstance();
            case REGISTERED_MEMBERS:
                return RegisteredMembersIdentity.getInstance();
            case MEMBER:
                return new MemberIdentity(identity.getPrimarySid());
            case ADMIN_GROUP:
                return AdministratorGroupIdentity.getInstance();
            default:
                return null;
        }
    }
}
