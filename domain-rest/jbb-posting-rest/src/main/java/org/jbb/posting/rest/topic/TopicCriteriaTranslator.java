/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.rest.topic;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.Direction.DESC;

@Component
public class TopicCriteriaTranslator {

    public PageRequest toForumView(TopicCriteriaDto criteria) {
        return toModel(criteria, DESC, "lastPost.updateDateTime");
    }

    public PageRequest toTopicView(TopicCriteriaDto criteria) {
        return toModel(criteria, ASC, "createDateTime");
    }

    private PageRequest toModel(TopicCriteriaDto criteria, Direction direction,
                                String sortProperty) {
        Integer page = Optional.ofNullable(criteria.getPage()).orElse(0);
        Integer pageSize = Optional.ofNullable(criteria.getPageSize()).orElse(20);

        return PageRequest.of(page, pageSize, Sort.by(direction, sortProperty));
    }
}
