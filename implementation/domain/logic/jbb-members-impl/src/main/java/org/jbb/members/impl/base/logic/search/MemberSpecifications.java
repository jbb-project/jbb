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


import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.MemberSearchCriteria.JoinMoment;
import org.jbb.members.impl.base.model.MemberEntity;
import org.springframework.data.jpa.domain.Specification;

import java.time.LocalDate;
import java.time.LocalTime;

public final class MemberSpecifications {
    private static final String VALUE = "value";
    private static final String JOIN_DATE_TIME = "joinDateTime";
    private static final String REGISTRATION_META_DATA = "registrationMetaData";

    private MemberSpecifications() {
        // not needed... just util class
    }

    public static Specification<MemberEntity> withUsername(Username username) {
        if (username != null && StringUtils.isNotBlank(username.getValue())) {
            return (root, cq, cb) ->
                    cb.like(cb.upper(root.get("username").get(VALUE)),
                            "%" + username.getValue().toUpperCase() + "%");
        } else {
            return null;
        }
    }

    public static Specification<MemberEntity> withDisplayedName(DisplayedName displayedname) {
        if (displayedname != null && StringUtils.isNotBlank(displayedname.getValue())) {
            return (root, cq, cb) ->
                    cb.like(cb.upper(root.get("displayedName").get(VALUE)),
                            "%" + displayedname.getValue().toUpperCase() + "%");
        } else {
            return null;
        }
    }

    public static Specification<MemberEntity> withEmail(Email email) {
        if (email != null && StringUtils.isNotBlank(email.getValue())) {
            return (root, cq, cb) ->
                    cb.like(cb.upper(root.get("email").get(VALUE)),
                            "%" + email.getValue().toUpperCase() + "%");
        } else {
            return null;
        }
    }

    public static Specification<MemberEntity> withJoinCriteria(LocalDate date, JoinMoment joinMoment) {
        if (date == null || joinMoment == null) {
            return null;
        }

        if (joinMoment.equals(JoinMoment.BEFORE)) {
            return (root, cq, cb) ->
                    cb.lessThan(root.get(REGISTRATION_META_DATA).get(JOIN_DATE_TIME), date.atTime(LocalTime.MIN));
        } else if (joinMoment.equals(JoinMoment.THAT_DAY)) {
            return (root, cq, cb) ->
                    cb.between(root.get(REGISTRATION_META_DATA).get(JOIN_DATE_TIME),
                            date.atTime(LocalTime.MIN),
                            date.atTime(LocalTime.MAX)
                    );
        } else {
            return (root, cq, cb) ->
                    cb.greaterThan(root.get(REGISTRATION_META_DATA).get(JOIN_DATE_TIME), date.atTime(LocalTime.MAX));
        }
    }
}
