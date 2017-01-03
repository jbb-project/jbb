/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lock.model;


import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Data;
import lombok.experimental.Tolerate;

@Builder
@Data
@Entity
@Table(name = "JBB_USER_LOCK_INVALID_SIGN_IN_ATTEMPT")
public class InvalidSignInAttemptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Column(name = "member_id")
    @NotNull
    private Long memberID;

    @Column(name = "invalid_attempt_date")
    @NotNull
    private LocalDateTime invalidAttemptDateTime;

    @Column(name = "ip_address")
    @NotNull
    private String ipAddress;

    @Tolerate
    InvalidSignInAttemptEntity() {
        memberID = new Long(-1);
        ipAddress = "";
        invalidAttemptDateTime = LocalDateTime.now();
    }


}
