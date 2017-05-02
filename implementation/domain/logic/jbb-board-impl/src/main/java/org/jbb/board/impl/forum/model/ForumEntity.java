/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.board.impl.forum.model;

import org.hibernate.envers.Audited;
import org.jbb.board.api.model.Forum;
import org.jbb.lib.db.domain.BaseEntity;

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
@Table(name = "JBB_BOARD_FORUM")
@Builder
@EqualsAndHashCode(callSuper = true)
public class ForumEntity extends BaseEntity implements Forum {

    private String name;

    private Integer position;

    private String description;

    private Boolean locked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ForumCategoryEntity category;

    @Tolerate
    ForumEntity() {
    }

    @Override
    public Boolean isLocked() {
        return locked;
    }
}
