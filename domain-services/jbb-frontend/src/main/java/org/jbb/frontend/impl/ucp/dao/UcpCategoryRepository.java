/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp.dao;

import org.jbb.frontend.impl.ucp.model.UcpCategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UcpCategoryRepository extends CrudRepository<UcpCategoryEntity, Long> {
    List<UcpCategoryEntity> findByOrderByOrdering();

}
