/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lockout.model;


import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_MEMBER_LOCKS")
@Builder
@EqualsAndHashCode(callSuper = true)
public class MemberLockEntity extends BaseEntity {

    @NotNull
    @Column(name = "member_id")
    private Long memberId;

    @NotNull
    @Column(name = "active")
    private Boolean active;

    @NotNull
    @Column(name = "expiration_date")
    private LocalDateTime expirationDate;

    @Column(name = "deactivation_date")
    private LocalDateTime deactivationDate;

    @Tolerate
    MemberLockEntity() {
        // for JPA
    }

}
