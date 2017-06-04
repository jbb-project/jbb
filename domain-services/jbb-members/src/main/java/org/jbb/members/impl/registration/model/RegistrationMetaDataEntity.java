/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.impl.registration.model;


import org.hibernate.envers.Audited;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.db.domain.BaseEntity;
import org.jbb.members.api.registration.RegistrationMetaData;

import java.time.LocalDateTime;

import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
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
@Table(name = "JBB_MEMBER_REGISTRATION_INFO")
@Builder
@EqualsAndHashCode(callSuper = true)
public class RegistrationMetaDataEntity extends BaseEntity implements RegistrationMetaData {

    @Column(name = "join_date_time")
    @NotNull
    private LocalDateTime joinDateTime;

    @Embedded
    @AttributeOverrides(@AttributeOverride(name = "value", column = @Column(name = "ip_address")))
    @Valid
    private IPAddress ipAddress;

    @Tolerate
    RegistrationMetaDataEntity() {
        // for JPA
        joinDateTime = LocalDateTime.now();
        ipAddress = IPAddress.builder().build();
    }
}
