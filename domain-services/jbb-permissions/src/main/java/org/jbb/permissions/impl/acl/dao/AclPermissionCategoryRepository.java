/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.acl.dao;

import org.jbb.permissions.impl.acl.model.AclPermissionCategoryEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AclPermissionCategoryRepository extends
        CrudRepository<AclPermissionCategoryEntity, Long> {

    @Deprecated
    AclPermissionCategoryEntity findAllByName(String name);

    AclPermissionCategoryEntity findAllByNameAndType(String name, AclPermissionTypeEntity type);
}
