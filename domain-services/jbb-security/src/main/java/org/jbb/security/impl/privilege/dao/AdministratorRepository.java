/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.privilege.dao;

import java.util.Optional;
import org.jbb.security.impl.privilege.model.AdministratorEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AdministratorRepository extends CrudRepository<AdministratorEntity, Long> {

    @Query("select a from AdministratorEntity a where a.memberId = :memberId")
    Optional<AdministratorEntity> findByMemberId(@Param("memberId") Long memberId);
}
