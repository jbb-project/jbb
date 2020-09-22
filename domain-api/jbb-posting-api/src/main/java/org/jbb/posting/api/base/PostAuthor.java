/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.api.base;

import org.apache.commons.lang3.Validate;

import java.util.Optional;

import javax.validation.constraints.NotNull;

public final class PostAuthor {

    @NotNull
    private final Optional<Long> authorMemberId;

    @NotNull
    private final Optional<String> anonAuthorName;

    private PostAuthor(Long authorMemberId) {
        Validate.notNull(authorMemberId);
        this.authorMemberId = Optional.of(authorMemberId);
        this.anonAuthorName = Optional.empty();
    }

    private PostAuthor(String anonAuthorName) {
        Validate.notBlank(anonAuthorName);
        this.authorMemberId = Optional.empty();
        this.anonAuthorName = Optional.of(anonAuthorName);
    }

    public static PostAuthor ofMember(Long memberId) {
        return new PostAuthor(memberId);
    }

    public static PostAuthor ofAnonymous(String anonName) {
        return new PostAuthor(anonName);
    }

    public boolean isAnonymous() {
        return anonAuthorName.isPresent();
    }

    public boolean isMember() {
        return authorMemberId.isPresent();
    }

    public Long getAuthorMemberId() {
        return authorMemberId.orElse(null);
    }

    public String getAnonAuthorName() {
        return anonAuthorName.orElse(null);
    }

}
