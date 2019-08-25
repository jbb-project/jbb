/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.rest.search;

import org.jbb.posting.api.search.PostSearchCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class PostCriteriaTranslator {

    public PostSearchCriteria toModel(PostCriteriaDto dto) {
        return PostSearchCriteria.builder()
                .phrase(dto.getQuery())
                .pageRequest(PageRequest.of(dto.getPage(), dto.getPageSize()))
                .build();
    }

}
