/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp.dao;

import org.jbb.frontend.impl.ucp.model.UcpElementEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UcpElementRepository extends CrudRepository<UcpElementEntity, Long> {
    @Query("SELECT e FROM UcpElementEntity e WHERE e.category.viewName = :viewName " +
            "ORDER BY e.ordering ASC")
    List<UcpElementEntity> findByCategoryNameOrderByOrdering(@Param("viewName") String viewName);

    @Query("SELECT e FROM UcpElementEntity e WHERE e.category.viewName = :categoryName AND e.viewName = :elementName")
    UcpElementEntity findByCategoryAndElementName(@Param("categoryName") String categoryName, @Param("elementName") String elementName);
}
