/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.base;

import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Optional;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
public class MemberSearchCriteria {

    public static final String USERNAME_COLUMN = "username";
    public static final String DISPLAYED_NAME_COLUMN = "displayedName";
    public static final String EMAIL_COLUMN = "email";
    public static final String JOIN_DATE_COLUMN = "registrationMetaData.joinDateTime";

    private Optional<Username> username = Optional.empty();
    private Optional<DisplayedName> displayedName = Optional.empty();
    private Optional<Email> email = Optional.empty();
    private Optional<JoinCriteria> joinCriteria = Optional.empty();
    @Setter
    private Pageable pageRequest = new PageRequest(0, 20);

    public void setUsername(Username username) {
        this.username = Optional.ofNullable(username);
    }

    public void setDisplayedName(DisplayedName displayedName) {
        this.displayedName = Optional.ofNullable(displayedName);
    }

    public void setEmail(Email email) {
        this.email = Optional.ofNullable(email);
    }

    public void setJoinCriteria(JoinCriteria joinCriteria) {
        this.joinCriteria = Optional.ofNullable(joinCriteria);
    }

    public enum JoinMoment {
        BEFORE, THAT_DAY, AFTER
    }

    @Getter
    @Setter
    @Builder
    public static class JoinCriteria {

        private LocalDate joinDate;

        private JoinMoment joinMoment;
    }

}
