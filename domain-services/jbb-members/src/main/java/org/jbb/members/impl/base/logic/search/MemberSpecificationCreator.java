/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.logic.search;

import static org.jbb.members.impl.base.logic.search.MemberSpecifications.withDisplayedName;
import static org.jbb.members.impl.base.logic.search.MemberSpecifications.withEmail;
import static org.jbb.members.impl.base.logic.search.MemberSpecifications.withJoinCriteria;
import static org.jbb.members.impl.base.logic.search.MemberSpecifications.withUsername;

import java.util.Optional;
import org.jbb.members.api.base.MemberSearchCriteria;
import org.jbb.members.api.base.MemberSearchCriteria.JoinCriteria;
import org.jbb.members.impl.base.model.MemberEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

@Component
public class MemberSpecificationCreator {

    public Specification<MemberEntity> createSpecification(MemberSearchCriteria criteria) {
        Specifications<MemberEntity> spec = Specifications.where(
            withUsername(criteria.getUsername().orElse(null)))
            .and(withDisplayedName(criteria.getDisplayedName().orElse(null)))
            .and(withEmail(criteria.getEmail().orElse(null))
            );

        Optional<JoinCriteria> joinCriteria = criteria.getJoinCriteria();
        if (joinCriteria.isPresent()) {
            spec = spec.and(withJoinCriteria(joinCriteria.get().getJoinDate(),
                joinCriteria.get().getJoinMoment()));
        }

        return spec;
    }
}
