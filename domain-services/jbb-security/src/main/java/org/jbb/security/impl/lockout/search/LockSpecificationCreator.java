/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout.search;

import static org.jbb.security.impl.lockout.search.LockSpecifications.withActive;
import static org.jbb.security.impl.lockout.search.LockSpecifications.withMemberId;

import org.jbb.security.api.lockout.LockSearchCriteria;
import org.jbb.security.impl.lockout.model.MemberLockEntity;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.domain.Specifications;
import org.springframework.stereotype.Component;

@Component
public class LockSpecificationCreator {

    public Specification<MemberLockEntity> createSpecification(LockSearchCriteria criteria) {
        return Specifications.where(withMemberId(criteria.getMemberId())
        ).and(withActive(criteria.getActive()));
    }
}
