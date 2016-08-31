/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.dao;

import org.jbb.lib.core.vo.Login;
import org.jbb.security.entities.PasswordEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordRepository extends CrudRepository<PasswordEntity, Long> {
    @Query("select p from PasswordEntity p where p.login = :login and " +
            "p.applicableSince = (select max(x.applicableSince) from PasswordEntity x where x.login = p.login) ")
    Optional<PasswordEntity> findTheNewestByLogin(@Param("login") Login login);
}
