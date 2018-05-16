/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.faq.model;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.jbb.frontend.api.faq.FaqCategory;
import org.jbb.frontend.api.faq.FaqEntry;
import org.jbb.lib.db.domain.BaseEntity;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_FRONTEND_FAQ_CATEGORIES")
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
public class FaqCategoryEntity extends BaseEntity implements FaqCategory {

    @NotBlank
    @Length(max = 255)
    private String name;

    @Min(1)
    private Integer position;

    @Valid
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
    private List<FaqEntryEntity> entries = Lists.newArrayList();

    @Tolerate
    FaqCategoryEntity() {
        // for JPA
    }

    @Override
    public List<FaqEntry> getQuestions() {
        return entries.stream().map(entity -> (FaqEntry) entity).collect(Collectors.toList());
    }
}
