/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.dao;

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.Member;
import org.jbb.members.impl.base.model.MemberEntity;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends CrudRepository<MemberEntity, Long>, JpaSpecificationExecutor<MemberEntity> {
    Long countByUsername(Username username);

    Long countByDisplayedName(DisplayedName displayedName);

    Member findByDisplayedName(DisplayedName displayedName);

    List<Member> findByEmail(Email email);

    Long countByEmail(Email email);

    @Query("SELECT m FROM MemberEntity m JOIN FETCH m.registrationMetaData " +
            "ORDER BY m.registrationMetaData.joinDateTime ASC")
    List<MemberEntity> findAllByOrderByRegistrationDateAsc();

    Optional<MemberEntity> findByUsername(Username username);
}
