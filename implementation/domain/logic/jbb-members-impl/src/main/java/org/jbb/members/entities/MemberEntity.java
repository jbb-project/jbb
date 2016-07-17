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

import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.Login;
import org.jbb.members.api.model.DisplayedName;
import org.jbb.members.api.model.MemberRegistrationAware;
import org.jbb.members.entities.validation.DisplayedNameNotBusy;
import org.jbb.members.entities.validation.EmailNotBusy;
import org.jbb.members.entities.validation.LoginNotBusy;

import java.io.Serializable;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Entity
@Table(name = "JBB_MEMBER")
@Builder
public class MemberEntity implements MemberRegistrationAware, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "login")))
    @NotNull
    @LoginNotBusy
    @Valid
    private Login login;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "displayedName")))
    @NotNull
    @DisplayedNameNotBusy
    @Valid
    private DisplayedName displayedName;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "email")))
    @NotNull
    @EmailNotBusy
    @Valid
    private Email email;

    @OneToOne(targetEntity = RegistrationMetaDataEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Valid
    private RegistrationMetaDataEntity registrationMetaData;

    @Tolerate
    MemberEntity() {
        // for JPA
        login = Login.builder().build();
        displayedName = DisplayedName.builder().build();
        email = Email.builder().build();
    }
}
