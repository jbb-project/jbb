/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.rememberme.dao;

import java.util.List;
import org.jbb.security.impl.rememberme.model.PersistentLoginEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PersistentLoginRepository extends CrudRepository<PersistentLoginEntity, Long> {

    PersistentLoginEntity findBySeries(String series);

    List<PersistentLoginEntity> findByMemberId(Long memberId);


}
