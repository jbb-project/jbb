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
import org.hibernate.validator.constraints.Length;
import org.jbb.board.api.forum.Forum;
import org.jbb.board.api.forum.ForumCategory;
import org.jbb.lib.db.domain.BaseEntity;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
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
@Table(name = "JBB_BOARD_FORUM_CATEGORIES")
@Builder
@EqualsAndHashCode(callSuper = true)
public class ForumCategoryEntity extends BaseEntity implements ForumCategory {

    @NotBlank
    @Length(min = 1, max = 255)
    private String name;

    @Min(1)
    private Integer position;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "category", cascade = CascadeType.ALL)
    private List<ForumEntity> forumEntities = Lists.newArrayList();

    @Tolerate
    ForumCategoryEntity() {
        // for JPA
    }

    @Override
    public List<Forum> getForums() {
        return forumEntities.stream()
                .sorted(Comparator.comparingInt(ForumEntity::getPosition))
                .map(entity -> (Forum) entity)
                .collect(Collectors.toList());
    }
}
