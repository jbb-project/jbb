/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.data;

import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.Username;

import java.time.LocalDate;
import java.util.Optional;

public interface MemberSearchCriteria {
    Optional<Username> withUsername();

    Optional<DisplayedName> withDisplayedName();

    Optional<Email> withEmail();

    Optional<JoinCriteria> withJoinCriteria();

    SortBy sortBy();


    enum JoinMoment {
        BEFORE, AFTER
    }

    enum SortColumn {
        USERNAME, DISPLAYED_NAME, EMAIL, JOIN_DATE
    }

    enum SortingOrder {
        ASC, DESC
    }

    interface JoinCriteria {
        LocalDate getJoinDate();

        JoinMoment getJoinMoment();
    }

    interface SortBy {
        SortColumn sortColumn();

        SortingOrder sortingOrder();
    }
}
