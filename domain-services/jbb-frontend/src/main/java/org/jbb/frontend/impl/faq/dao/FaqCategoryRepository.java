/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.faq.dao;

import org.jbb.frontend.impl.faq.model.FaqCategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FaqCategoryRepository extends CrudRepository<FaqCategoryEntity, Long> {

    List<FaqCategoryEntity> findByOrderByPosition();
}
