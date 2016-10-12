/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lock.model;

import org.jbb.lib.core.vo.Username;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;
import lombok.experimental.Tolerate;

@Data
@Entity
@Table(name = "JBB_USER_LOCK_INVALID_SIGN_IN_ATTEMPT")
public class InvalidSignInAttemptEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long id;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "username")))
    @NotNull
    private Username username;

    @Column
    private int invalidSignInAttempt;

    @Column(name = "lastInvalidSignInDate")
    private LocalDateTime localDateTime;

    @Tolerate
    InvalidSignInAttemptEntity() {
        username = Username.builder().build();
        invalidSignInAttempt = -1;
        localDateTime = LocalDateTime.now();
    }


}
