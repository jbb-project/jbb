/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.entities;

import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.Email;
import org.jbb.members.api.model.Login;
import org.jbb.members.api.model.Member;
import org.jbb.members.api.model.RegistrationDate;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Data;

@Data
@Entity
@Table(name = "JBB_MEMBER")
@Builder
public class MemberEntity implements Member, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "login")))
    private Login login;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "displayedName")))
    private DisplayedName displayedName;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "email")))
    private Email email;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "registrationDate")))
    private RegistrationDate registrationDate;
}
