/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.role.model;

import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;
import org.jbb.permissions.api.entry.PermissionValue;
import org.jbb.permissions.impl.acl.model.AclPermissionEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
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
@Table(name = "JBB_ACL_ROLE_ENTITIES")
@Builder
@EqualsAndHashCode(callSuper = true)
public class AclRoleEntryEntity extends BaseEntity {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_id")
    private AclPermissionEntity permission;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_id")
    private AclRoleEntity role;

    @Column(name = "entry_value")
    @Enumerated(EnumType.STRING)
    private PermissionValue entryValue;

    @Tolerate
    AclRoleEntryEntity() {
        // for JPA
    }

}
