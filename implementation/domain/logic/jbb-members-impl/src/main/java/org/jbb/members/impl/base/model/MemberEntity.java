/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.base.model;

import org.hibernate.envers.Audited;
import org.jbb.lib.core.vo.Email;
import org.jbb.lib.core.vo.Username;
import org.jbb.lib.db.domain.BaseEntity;
import org.jbb.members.api.data.DisplayedName;
import org.jbb.members.api.data.MemberRegistrationAware;
import org.jbb.members.impl.base.model.validation.DisplayedNameNotBusy;
import org.jbb.members.impl.base.model.validation.EmailNotBusy;
import org.jbb.members.impl.base.model.validation.UsernameNotBusy;
import org.jbb.members.impl.registration.model.RegistrationMetaDataEntity;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_MEMBER")
@Builder
@EmailNotBusy
@UsernameNotBusy
@DisplayedNameNotBusy
@EqualsAndHashCode(callSuper = true)
public class MemberEntity extends BaseEntity implements MemberRegistrationAware {

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "username")))
    @NotNull
    @Valid
    private Username username;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "displayed_name")))
    @NotNull
    @Valid
    private DisplayedName displayedName;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "email")))
    @NotNull
    @Valid
    private Email email;

    @OneToOne(targetEntity = RegistrationMetaDataEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Valid
    private RegistrationMetaDataEntity registrationMetaData;

    @Tolerate
    MemberEntity() {
        // for JPA
        username = Username.builder().build();
        displayedName = DisplayedName.builder().build();
        email = Email.builder().build();
    }

}
