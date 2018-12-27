/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.logic;

import org.apache.commons.lang3.EnumUtils;
import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.base.MemberSearchCriteria;
import org.jbb.members.api.base.MemberSearchCriteria.JoinCriteria;
import org.jbb.members.api.base.MemberSearchCriteria.JoinMoment;
import org.jbb.members.api.base.MemberSearchJoinDateFormatException;
import org.jbb.members.web.base.form.SearchMemberForm;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class MemberSearchCriteriaFactory {

    public MemberSearchCriteria buildCriteria(SearchMemberForm form, Pageable pageable) {
        MemberSearchCriteria criteria = new MemberSearchCriteria();
        criteria.setUsername(Username.builder().value(form.getUsername()).build());
        criteria.setDisplayedName(DisplayedName.builder().value(form.getDisplayedName()).build());
        criteria.setEmail(Email.builder().value(form.getEmail()).build());
        criteria.setJoinCriteria(buildJoinCriteria(form));
        criteria.setPageRequest(includeSortingToPageable(form, pageable));

        return criteria;
    }

    private JoinCriteria buildJoinCriteria(SearchMemberForm form) {
        LocalDate joinDate = getJoinDate(form);
        return joinDate == null ? null : JoinCriteria.builder()
            .joinDate(joinDate)
            .joinMoment(getJoinMoment(form))
            .build();
    }

    private LocalDate getJoinDate(SearchMemberForm form) {
        try {
            return StringUtils.isNotBlank(form.getJoinedDate()) ?
                    LocalDate.parse(form.getJoinedDate(),
                            DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
        } catch (DateTimeParseException e) {
            log.trace("Date time parsing error", e);
            throw new MemberSearchJoinDateFormatException();
        }
    }

    private JoinMoment getJoinMoment(SearchMemberForm form) {
        return EnumUtils.getEnum(MemberSearchCriteria.JoinMoment.class, form.getJoinedMoment());
    }

    private Pageable includeSortingToPageable(SearchMemberForm form, Pageable pageable) {
        return PageRequest.of(pageable.getPageNumber(),
                pageable.getPageSize(),
                Direction.fromString(form.getSortDirection()),
                form.getSortByField());
    }
}
