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

import org.jbb.permissions.impl.acl.model.AclSecurityIdentityTypeEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AclSecurityIdentityTypeRepository extends
        CrudRepository<AclSecurityIdentityTypeEntity, Long> {

    AclSecurityIdentityTypeEntity findAllByName(String name);

}
