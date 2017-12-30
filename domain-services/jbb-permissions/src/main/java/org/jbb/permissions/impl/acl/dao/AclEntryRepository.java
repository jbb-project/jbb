/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.acl.dao;

import org.jbb.permissions.impl.acl.model.AclEntryEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionEntity;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AclEntryRepository extends CrudRepository<AclEntryEntity, Long> {

    @Modifying
    void deleteAllBySecurityIdentityAndPermission(AclSecurityIdentityEntity securityIdentity,
                                                  AclPermissionEntity permission);

    Optional<AclEntryEntity> findBySecurityIdentityAndPermission(
            AclSecurityIdentityEntity securityIdentity, AclPermissionEntity permission);

}
