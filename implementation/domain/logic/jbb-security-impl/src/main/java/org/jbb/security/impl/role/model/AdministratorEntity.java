/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.role.model;

import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_ADMINISTRATOR")
@Builder
public class AdministratorEntity extends BaseEntity {

    @NotNull
    @Column(name = "member_id")
    private Long memberId;

    @Tolerate
    AdministratorEntity() {
        memberId = 0L;
        // for JPA
    }
}
