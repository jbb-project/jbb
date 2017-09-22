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

import lombok.RequiredArgsConstructor;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.impl.acl.dao.AclPermissionTypeRepository;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PermissionTypeEntityResolver {

    private final AclPermissionTypeRepository aclPermissionTypeRepository;

    public AclPermissionTypeEntity resolve(PermissionType type) {
        return aclPermissionTypeRepository.findAllByName(type.name());
    }

}
