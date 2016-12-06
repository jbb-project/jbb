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

import org.jbb.frontend.impl.acp.model.AcpElementEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AcpElementRepository extends CrudRepository<AcpElementEntity, Long> {
    @Query("SELECT e FROM AcpElementEntity e " +
            "WHERE e.subcategory.category.viewName = :categoryName " +
            "AND e.viewName = :elementName")
    AcpElementEntity findByCategoryAndElementName(@Param("categoryName") String categoryViewName,
                                                  @Param("elementName") String elementViewName);

}
