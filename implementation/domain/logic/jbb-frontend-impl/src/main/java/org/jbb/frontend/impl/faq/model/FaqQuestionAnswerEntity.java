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

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.NotBlank;
import org.jbb.frontend.api.faq.FaqQuestionAnswer;
import org.jbb.lib.db.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "JBB_FRONTEND_FAQ_QUESTION_ANSWER")
@Builder
public class FaqQuestionAnswerEntity extends BaseEntity implements FaqQuestionAnswer {

    @Min(1)
    private Integer position;

    @NotBlank
    private String question;

    @NotBlank
    private String answer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private FaqCategoryEntity category;

    @Tolerate
    FaqQuestionAnswerEntity() {
        // for JPA...
    }
}
