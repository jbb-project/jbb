/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.role.dao;

import org.jbb.permissions.api.role.PredefinedRole;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.jbb.permissions.impl.role.model.AclRoleEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AclRoleRepository extends CrudRepository<AclRoleEntity, Long> {

    Optional<AclRoleEntity> findTopByPermissionTypeOrderByPositionDesc(
            AclPermissionTypeEntity permissionType);

    List<AclRoleEntity> findAllByPermissionTypeOrderByPositionAsc(
            AclPermissionTypeEntity permissionType);

    List<AclRoleEntity> findAllByPermissionTypeAndPredefinedRoleNotNullOrderByPositionAsc(
            AclPermissionTypeEntity permissionType);

    AclRoleEntity findByPredefinedRole(PredefinedRole predefinedRole);
}
