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
import org.jbb.lib.commons.vo.Email;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.db.domain.BaseEntity;
import org.jbb.members.api.base.DisplayedName;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.jbb.members.impl.base.model.validation.create.CreateGroup;
import org.jbb.members.impl.base.model.validation.create.DisplayedNameNotBusyCreate;
import org.jbb.members.impl.base.model.validation.create.EmailNotBusyCreate;
import org.jbb.members.impl.base.model.validation.create.UsernameNotBusyCreate;
import org.jbb.members.impl.base.model.validation.update.DisplayedNameNotBusyUpdate;
import org.jbb.members.impl.base.model.validation.update.EmailNotBusyUpdate;
import org.jbb.members.impl.base.model.validation.update.UpdateGroup;
import org.jbb.members.impl.base.model.validation.update.UsernameNotBusyUpdate;
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
@Table(name = "JBB_MEMBERS")
@Builder
@UsernameNotBusyUpdate(groups = UpdateGroup.class)
@EmailNotBusyUpdate(groups = UpdateGroup.class)
@DisplayedNameNotBusyUpdate(groups = UpdateGroup.class)
@EqualsAndHashCode(callSuper = true)
public class MemberEntity extends BaseEntity implements MemberRegistrationAware {

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "username")))
    @NotNull
    @Valid
    @UsernameNotBusyCreate(groups = CreateGroup.class)
    private Username username;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "displayed_name")))
    @NotNull
    @Valid
    @DisplayedNameNotBusyCreate(groups = CreateGroup.class)
    private DisplayedName displayedName;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "email")))
    @NotNull
    @Valid
    @EmailNotBusyCreate(groups = CreateGroup.class)
    private Email email;

    @OneToOne(targetEntity = RegistrationMetaDataEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Valid
    private RegistrationMetaDataEntity registrationMetaData;

    @Tolerate
    MemberEntity() {
        // for JPA
    }

}
