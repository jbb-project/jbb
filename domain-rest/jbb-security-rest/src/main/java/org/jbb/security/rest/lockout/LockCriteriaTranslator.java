/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.rest.lockout;

import org.jbb.security.api.lockout.LockSearchCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

@Component
public class LockCriteriaTranslator {

    public LockSearchCriteria toModel(LockCriteriaDto dto) {
        return LockSearchCriteria.builder()
            .memberId(dto.getMemberId())
            .active(dto.getActive())
                .pageRequest(PageRequest.of(dto.getPage(), dto.getPageSize()))
            .build();
    }

}
