/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.impl.base.dao;

import org.jbb.posting.impl.base.model.PostEntity;
import org.jbb.posting.impl.base.model.TopicEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends CrudRepository<PostEntity, Long> {

    Page<PostEntity> findByTopic(TopicEntity topic, Pageable pageable);

    List<PostEntity> findByTopic(TopicEntity topic);

    List<PostEntity> findByMemberId(Long memberId);
}
