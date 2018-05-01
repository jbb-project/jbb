/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.role.dao;

import org.jbb.permissions.impl.acl.model.AclPermissionEntity;
import org.jbb.permissions.impl.role.model.AclRoleEntity;
import org.jbb.permissions.impl.role.model.AclRoleEntryEntity;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AclRoleEntryRepository extends
        PagingAndSortingRepository<AclRoleEntryEntity, Long> {

    Optional<AclRoleEntryEntity> findByRoleAndPermission(AclRoleEntity role,
                                                         AclPermissionEntity permission);


    List<AclRoleEntryEntity> findAllByRole(AclRoleEntity role, Sort sort);


}
