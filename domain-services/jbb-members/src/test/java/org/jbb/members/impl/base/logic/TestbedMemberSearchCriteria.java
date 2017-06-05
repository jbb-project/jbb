/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.logic;

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.MemberSearchCriteria;

import java.time.LocalDate;
import java.util.Optional;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestbedMemberSearchCriteria implements MemberSearchCriteria {
    private static final SortBy DEFAULT_SORT_BY = new SortBy() {
        @Override
        public SortColumn sortColumn() {
            return SortColumn.JOIN_DATE;
        }

        @Override
        public SortingOrder sortingOrder() {
            return SortingOrder.ASC;
        }
    };

    private Optional<Username> username = Optional.empty();
    private Optional<DisplayedName> displayedName = Optional.empty();
    private Optional<Email> email = Optional.empty();
    private Optional<JoinCriteria> joinCriteria = Optional.empty();
    private SortBy sortBy = DEFAULT_SORT_BY;

    public void setUsername(Username username) {
        this.username = Optional.of(username);
    }

    public void setDisplayedName(DisplayedName displayedName) {
        this.displayedName = Optional.of(displayedName);
    }

    public void setEmail(Email email) {
        this.email = Optional.of(email);
    }

    public void setJoinCriteria(LocalDate joinDate, JoinMoment joinMoment) {
        JoinCriteria criteria = new JoinCriteria() {
            @Override
            public LocalDate getJoinDate() {
                return joinDate;
            }

            @Override
            public JoinMoment getJoinMoment() {
                return joinMoment;
            }
        };

        joinCriteria = Optional.ofNullable(criteria);
    }

    public void setSortBy(SortColumn sortColumn, SortingOrder sortingOrder) {
        sortBy = new SortBy() {
            @Override
            public SortColumn sortColumn() {
                return sortColumn;
            }

            @Override
            public SortingOrder sortingOrder() {
                return sortingOrder;
            }
        };
    }

    @Override
    public Optional<Username> withUsername() {
        return username;
    }

    @Override
    public Optional<DisplayedName> withDisplayedName() {
        return displayedName;
    }

    @Override
    public Optional<Email> withEmail() {
        return email;
    }

    @Override
    public Optional<JoinCriteria> withJoinCriteria() {
        return joinCriteria;
    }

    @Override
    public SortBy sortBy() {
        return sortBy;
    }
}
