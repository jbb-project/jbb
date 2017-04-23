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

import com.google.common.collect.Lists;

import org.hibernate.envers.Audited;
import org.jbb.board.api.model.Forum;
import org.jbb.board.api.model.ForumCategory;
import org.jbb.lib.db.domain.BaseEntity;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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
@Table(name = "JBB_BOARD_FORUM_CATEGORY")
@Builder
@EqualsAndHashCode(callSuper = true)
public class ForumCategoryEntity extends BaseEntity implements ForumCategory {

    private String name;

    private Integer ordering;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
    private List<ForumEntity> forumEntities;

    @Tolerate
    ForumCategoryEntity() {
        forumEntities = Lists.newArrayList();
    }

    @Override
    public List<Forum> getForums() {
        return forumEntities.stream()
                .map(entity -> (Forum) entity)
                .collect(Collectors.toList());
    }
}
