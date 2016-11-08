/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lock.dao;


import org.jbb.lib.core.vo.Username;
import org.jbb.security.impl.lock.model.InvalidSignInAttemptEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InvalidSignInAttemptRepository extends CrudRepository<InvalidSignInAttemptEntity, Long> {

    Optional<InvalidSignInAttemptEntity> findByUsername(Username username);

    @Query("delete from InvalidSignInAttemptEntity p WHERE p.username = :username")
    void clearInvalidSigInAttemptForSpecifyUser(@Param("username") Username username);

    @Query("select count(p) from InvalidSignInAttemptEntity p WHERE p.username = :username")
    int getNumberOfInvalidSignInAttempts(@Param("username") Username username);

}
