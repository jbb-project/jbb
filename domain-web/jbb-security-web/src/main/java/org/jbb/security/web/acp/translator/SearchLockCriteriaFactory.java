/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.web.acp.translator;

import org.jbb.security.api.lockout.LockSearchCriteria;
import org.jbb.security.web.acp.form.SearchLockForm.LockStatus;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class SearchLockCriteriaFactory {

    public LockSearchCriteria buildCriteria(Long memberId, LockStatus lockStatus,
        Pageable pageable) {
        return LockSearchCriteria.builder()
            .memberId(memberId)
            .active(resolveActive(lockStatus))
            .pageRequest(pageable)
            .build();
    }

    private Boolean resolveActive(LockStatus lockStatus) {
        if (lockStatus == LockStatus.ACTIVE) {
            return true;
        } else if (lockStatus == LockStatus.INACTIVE) {
            return false;
        }
        return null; //NOSONAR
    }

}
