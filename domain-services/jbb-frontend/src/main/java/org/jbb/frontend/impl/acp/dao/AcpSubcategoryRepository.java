/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp.dao;

import org.jbb.frontend.impl.acp.model.AcpSubcategoryEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AcpSubcategoryRepository extends CrudRepository<AcpSubcategoryEntity, Long> {
    @Query("SELECT s FROM AcpSubcategoryEntity s " +
            "WHERE s.category.viewName = :categoryName " +
            "ORDER BY s.ordering ASC")
    List<AcpSubcategoryEntity> findByCategoryOrderByOrdering(@Param("categoryName") String categoryViewName);

}
