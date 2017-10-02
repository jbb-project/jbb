/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.dao;

import org.jbb.security.impl.password.model.PasswordEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface PasswordRepository extends CrudRepository<PasswordEntity, Long> {

    @Query("select p from PasswordEntity p where p.memberId = :memberId and " +
            "p.applicableSince = (select max(x.applicableSince) from PasswordEntity x where x.memberId = p.memberId) ")
    Optional<PasswordEntity> findTheNewestByMemberId(@Param("memberId") Long memberId);

    @Query("delete from PasswordEntity p where p.memberId = :memberId")
    @Modifying
    @Transactional
    void removeByMemberId(@Param("memberId") Long memberId);
}
