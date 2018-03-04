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

import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityEntity;
import org.jbb.permissions.impl.role.model.AclActiveRoleEntity;
import org.jbb.permissions.impl.role.model.AclRoleEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AclActiveRoleRepository extends CrudRepository<AclActiveRoleEntity, Long> {

    @Query("SELECT a FROM AclActiveRoleEntity a JOIN FETCH a.role as r WHERE " +
            " r.permissionType = :permissionType AND a.securityIdentity = :securityIdentity")
    Optional<AclActiveRoleEntity> findActiveByPermissionTypeAndSecurityIdentity(
            @Param("permissionType") AclPermissionTypeEntity permissionType,
            @Param("securityIdentity") AclSecurityIdentityEntity securityIdentity);

    @Modifying
    void deleteAllBySecurityIdentity(AclSecurityIdentityEntity securityIdentity);

    List<AclActiveRoleEntity> findByRole(AclRoleEntity role);

}
