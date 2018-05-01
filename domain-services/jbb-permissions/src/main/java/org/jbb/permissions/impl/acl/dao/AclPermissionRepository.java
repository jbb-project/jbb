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

import org.jbb.permissions.impl.acl.model.AclPermissionEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AclPermissionRepository extends CrudRepository<AclPermissionEntity, Long> {

    AclPermissionEntity findAllByCode(String code);

    @Query("SELECT p FROM AclPermissionEntity p WHERE p.category.type = :permissionType")
    List<AclPermissionEntity> findAllByPermissionType(@Param("permissionType") AclPermissionTypeEntity permissionType);

}
