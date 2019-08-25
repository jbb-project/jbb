/*
 * Copyright (C) 2019 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.impl.base.model;

import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
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
@Table(name = "JBB_POSTS")
@Builder
@EqualsAndHashCode(callSuper = true)
public class PostEntity extends BaseEntity {

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "topic_id")
    private TopicEntity topic;

    @Column(name = "anonymous_name")
    private String anonymousName;

    @Column(name = "member_id")
    private Long memberId;

    @NotBlank
    private String subject;

    @Valid
    @NotNull
    @OneToOne(targetEntity = PostContentEntity.class, cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "post_content_id")
    private PostContentEntity postContent;

    @Tolerate
    PostEntity() {
        // for JPA
    }

}
