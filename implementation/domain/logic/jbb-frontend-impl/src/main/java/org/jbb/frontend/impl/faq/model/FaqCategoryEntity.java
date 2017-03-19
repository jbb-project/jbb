/*
 * Copyright (C) 2017 the original author or authors.
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
import org.hibernate.validator.constraints.NotBlank;
import org.jbb.frontend.api.faq.FaqCategory;
import org.jbb.frontend.api.faq.FaqQuestionAnswer;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Min;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_FRONTEND_FAQ_CATEGORY")
@Builder
public class FaqCategoryEntity implements FaqCategory, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @Min(1)
    private Integer position;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
    private List<FaqQuestionAnswerEntity> questionsAnswers;

    @Tolerate
    FaqCategoryEntity() {
        questionsAnswers = Lists.newArrayList();
        // for JPA...
    }

    @Override
    public List<FaqQuestionAnswer> getQuestions() {
        return questionsAnswers.stream()
                .map(entity -> (FaqQuestionAnswer) entity)
                .collect(Collectors.toList());
    }
}
