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

import javax.persistence.Column;
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
import org.hibernate.validator.constraints.NotBlank;
import org.jbb.lib.db.domain.BaseEntity;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_ACL_PERMISSIONS")
@Builder
@EqualsAndHashCode(callSuper = true)
public class AclPermissionEntity extends BaseEntity {

    @NotBlank
    private String name;

    @NotBlank
    @Column(unique = true)
    private String code;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private AclPermissionCategoryEntity category;

    @NotNull
    @Min(0)
    private Integer position;

    @Tolerate
    AclPermissionEntity() {
        // for JPA
    }

}
