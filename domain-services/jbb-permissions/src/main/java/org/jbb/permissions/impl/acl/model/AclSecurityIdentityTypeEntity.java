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

import com.google.common.collect.Lists;

import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
@Table(name = "JBB_ACL_SECURITY_IDENTITY_TYPES")
@Builder
@EqualsAndHashCode(callSuper = true)
public class AclSecurityIdentityTypeEntity extends BaseEntity {

    @NotNull
    private String name;

    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "type", cascade = CascadeType.ALL)
    private List<AclSecurityIdentityEntity> securityIdentities = Lists.newArrayList();

    @Tolerate
    AclSecurityIdentityTypeEntity() {
        // for JPA
    }

}
