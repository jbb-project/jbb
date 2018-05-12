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

import java.util.List;
import java.util.Optional;
import org.jbb.security.impl.lockout.model.MemberLockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberLockRepository extends JpaRepository<MemberLockEntity, Long> {

    Optional<MemberLockEntity> findByMemberIdAndActiveTrue(long memberId);

    List<MemberLockEntity> findByMemberId(long memberId);
}
