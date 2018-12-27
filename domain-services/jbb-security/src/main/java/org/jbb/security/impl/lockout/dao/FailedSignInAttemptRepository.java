/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout.dao;


import org.jbb.security.impl.lockout.model.FailedSignInAttemptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailedSignInAttemptRepository extends JpaRepository<FailedSignInAttemptEntity, Long> {

    @Query("select p from FailedSignInAttemptEntity p WHERE p.memberId = :memberId order by p.attemptDateTime asc")
    List<FailedSignInAttemptEntity> findAllForMemberOrderByDateAsc(@Param("memberId") Long memberId);

    @Modifying
    @Query("delete from FailedSignInAttemptEntity p WHERE p.memberId = ?1")
    void deleteAllForMember(@Param("memberId") Long memberId);

    @Query("select p from FailedSignInAttemptEntity p WHERE p.memberId = :memberId")
    List<FailedSignInAttemptEntity> findAllForMember(@Param("memberId") Long memberId);

}
