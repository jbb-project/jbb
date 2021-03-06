/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.forum.model;

import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;
import org.jbb.board.api.forum.Forum;
import org.jbb.lib.db.domain.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
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
@Table(name = "JBB_BOARD_FORUMS")
@Builder
@EqualsAndHashCode(callSuper = true)
public class ForumEntity extends BaseEntity implements Forum {

    @NotBlank
    @Length(min = 1, max = 255)
    private String name;

    @Min(1)
    private Integer position;

    @Lob
    private String description;

    @NotNull
    private Boolean closed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ForumCategoryEntity category;

    @Tolerate
    ForumEntity() {
        // for JPA
    }

    @Override
    public Boolean isClosed() {
        return closed;
    }
}
