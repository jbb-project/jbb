/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.dao;

import org.jbb.lib.core.vo.Email;
import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.Login;
import org.jbb.members.entities.MemberEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends CrudRepository<MemberEntity, Long> {
    Long countByLogin(Login login);

    Long countByDisplayedName(DisplayedName displayedName);

    Long countByEmail(Email email);

    @Query("SELECT m FROM MemberEntity m JOIN FETCH m.registrationMetaData " +
            "ORDER BY m.registrationMetaData.joinDateTime ASC")
    List<MemberEntity> findAllByOrderByRegistrationDateAsc();
}
