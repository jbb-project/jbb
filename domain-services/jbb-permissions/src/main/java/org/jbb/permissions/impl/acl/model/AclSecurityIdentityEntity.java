/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.acl.model;

import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
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
@Table(name = "JBB_ACL_SECURITY_IDENTITIES")
@Builder
@EqualsAndHashCode(callSuper = true)
public class AclSecurityIdentityEntity extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "type_id")
    private AclSecurityIdentityTypeEntity type;

    @Column(name = "primary_sid")
    private Long primarySid;

    @Column(name = "secondary_sid")
    private Long secondarySid;

    @Tolerate
    AclSecurityIdentityEntity() {
        // for JPA
    }

}
