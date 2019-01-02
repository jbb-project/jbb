/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.faq.model;

import com.google.common.collect.Lists;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.jbb.lib.db.domain.BaseEntity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_FRONTEND_FAQ_CATEGORIES")
@Builder
@EqualsAndHashCode(callSuper = true)
public class FaqCategoryEntity extends BaseEntity {

    @NotBlank
    @Length(max = 255)
    private String name;

    @Min(1)
    private Integer position;

    @Valid
    @Builder.Default
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
    private List<FaqEntryEntity> entries = Lists.newArrayList();

    @Tolerate
    FaqCategoryEntity() {
        // for JPA
    }

}
