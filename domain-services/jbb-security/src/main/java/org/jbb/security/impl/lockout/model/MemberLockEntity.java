/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout.model;


import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;
import org.jbb.security.api.model.MemberLock;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.experimental.Tolerate;

@Getter
@Entity
@Audited
@Table(name = "JBB_MEMBER_LOCK")
@Builder
@EqualsAndHashCode(callSuper = true)
public class MemberLockEntity extends BaseEntity implements MemberLock {

    @NotNull
    @Column(name = "member_id")
    private Long memberId;

    @NotNull
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Tolerate
    MemberLockEntity() {
        memberId = -1L;
        expirationDate = LocalDateTime.now();
    }

}
