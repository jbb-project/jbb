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

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_ACL_ROLES")
@Builder
@EqualsAndHashCode(callSuper = true)
public class AclRoleEntity extends BaseEntity {

    private String name;

    private String description;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "permission_type_id")
    private AclPermissionTypeEntity permissionType;

    @NotNull
    @Min(0)
    private Integer position;

    @Tolerate
    AclRoleEntity() {
        // for JPA
    }

}