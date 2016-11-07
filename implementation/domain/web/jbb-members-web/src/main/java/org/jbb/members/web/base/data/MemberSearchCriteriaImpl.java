/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.data;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.MemberSearchCriteria;
import org.jbb.members.web.base.form.SearchMemberForm;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class MemberSearchCriteriaImpl implements MemberSearchCriteria {
    private Optional<Username> username;
    private Optional<DisplayedName> displayedName;
    private Optional<Email> email;
    private Optional<JoinCriteria> joinCriteria;
    private SortBy sortBy;

    public MemberSearchCriteriaImpl(SearchMemberForm form) {
        username = Optional.ofNullable(Username.builder().value(form.getUsername()).build());
        displayedName = Optional.ofNullable(DisplayedName.builder().value(form.getDisplayedName()).build());
        email = Optional.ofNullable(Email.builder().value(form.getEmail()).build());

        JoinCriteria criteria = new JoinCriteria() {
            @Override
            public LocalDate getJoinDate() {
                return StringUtils.isNotBlank(form.getJoinedDate()) ?
                        LocalDate.parse(form.getJoinedDate(),
                                DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
            }

            @Override
            public JoinMoment getJoinMoment() {
                return EnumUtils.getEnum(JoinMoment.class, form.getJoinedMoment());
            }
        };

        joinCriteria = Optional.ofNullable(criteria);

        sortBy = new SortBy() {
            @Override
            public SortColumn sortColumn() {
                return EnumUtils.getEnum(SortColumn.class, form.getSortByField());
            }

            @Override
            public SortingOrder sortingOrder() {
                return EnumUtils.getEnum(SortingOrder.class, form.getSortDirection());
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
