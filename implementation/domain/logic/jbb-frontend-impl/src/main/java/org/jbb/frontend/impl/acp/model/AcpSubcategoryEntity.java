/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp.model;

import com.google.common.collect.Lists;

import org.jbb.frontend.api.model.AcpSubcategory;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Entity
@Table(name = "JBB_FRONTEND_ACP_SUBCATEGORY")
@Builder
public class AcpSubcategoryEntity implements AcpSubcategory, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer ordering;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private AcpCategoryEntity category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "subcategory", cascade = CascadeType.ALL)
    private List<AcpElementEntity> elements;

    @Tolerate
    public AcpSubcategoryEntity() {
        elements = Lists.newArrayList();
        // for JPA...
    }
}
