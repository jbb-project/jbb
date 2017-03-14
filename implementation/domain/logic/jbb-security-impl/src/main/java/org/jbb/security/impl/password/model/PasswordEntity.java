/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.password.model;

import org.apache.commons.lang3.StringUtils;
import org.jbb.lib.core.vo.Password;
import org.jbb.security.impl.password.model.validation.PasswordRequirementsSatisfied;

import java.io.Serializable;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Entity
@Table(name = "JBB_PASSWORD")
@Builder
public class PasswordEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "member_id")
    private Long memberId;

    @Column(name = "password")
    @NotNull
    private String password;

    @Column(name = "applicable_since")
    @NotNull
    private LocalDateTime applicableSince;

    @Transient
    @Valid
    @PasswordRequirementsSatisfied
    private String visiblePassword;

    @Tolerate
    PasswordEntity() {
        // for JPA
        password = StringUtils.EMPTY;
        applicableSince = LocalDateTime.now();
    }

    public Password getPasswordValueObject() {
        return Password.builder().value(password.toCharArray()).build();
    }
}
