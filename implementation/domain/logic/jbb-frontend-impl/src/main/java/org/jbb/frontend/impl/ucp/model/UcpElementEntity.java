/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.ucp.model;

import org.hibernate.envers.Audited;
import org.jbb.frontend.api.ucp.UcpElement;
import org.jbb.lib.db.domain.BaseEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_FRONTEND_UCP_ELEMENT")
@Builder
@EqualsAndHashCode(callSuper = true)
public class UcpElementEntity extends BaseEntity implements UcpElement {

    private String name;

    private Integer ordering;

    @Column(name = "view_name")
    private String viewName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private UcpCategoryEntity category;

    @Tolerate
    UcpElementEntity() {
        // for JPA...
    }
}
