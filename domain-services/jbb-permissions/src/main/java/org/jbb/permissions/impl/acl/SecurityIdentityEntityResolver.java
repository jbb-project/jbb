/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.acl;

import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.identity.SecurityIdentity.Type;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityRepository;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityTypeRepository;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityEntity;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityTypeEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SecurityIdentityEntityResolver {

    private final AclSecurityIdentityTypeRepository securityIdentityTypeRepository;
    private final AclSecurityIdentityRepository aclSecurityIdentityRepository;

    public Optional<AclSecurityIdentityEntity> resolve(SecurityIdentity securityIdentity) {
        AclSecurityIdentityTypeEntity identityType = securityIdentityTypeRepository
            .findAllByName(securityIdentity.getType().name());
        if (securityIdentity.getType() == Type.MEMBER) {
            return aclSecurityIdentityRepository
                .findByTypeAndPrimarySid(identityType, securityIdentity.getId());
        } else {
            return Optional.ofNullable(aclSecurityIdentityRepository.findTopByType(identityType));
        }
    }

}
