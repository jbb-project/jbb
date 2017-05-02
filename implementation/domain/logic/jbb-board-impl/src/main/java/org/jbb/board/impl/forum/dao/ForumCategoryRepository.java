/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.forum.dao;

import org.jbb.board.impl.forum.model.ForumCategoryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ForumCategoryRepository extends CrudRepository<ForumCategoryEntity, Long> {
    List<ForumCategoryEntity> findAllByOrderByPositionAsc();

    Optional<ForumCategoryEntity> findTopByOrderByPositionDesc();
}
