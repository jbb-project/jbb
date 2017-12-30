/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.base;

import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.MemberSearchCriteria;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class MemberCriteriaTranslator {

    public MemberSearchCriteria toModel(MemberCriteriaDto criteriaDto) {

        String displayedName = criteriaDto.getDisplayedName();
        Integer page = criteriaDto.getPage();
        Integer pageSize = criteriaDto.getPageSize();

        MemberSearchCriteria searchCriteria = new MemberSearchCriteria();
        searchCriteria.setDisplayedName(displayedName == null ? null :
                DisplayedName.builder().value(displayedName).build());
        searchCriteria.setPageRequest(new PageRequest(
                Optional.ofNullable(page).orElse(0), Optional.ofNullable(pageSize).orElse(20)
        ));

        return searchCriteria;
    }

}
