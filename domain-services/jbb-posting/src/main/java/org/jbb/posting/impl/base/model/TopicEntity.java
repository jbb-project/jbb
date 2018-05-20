/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.posting.impl.base.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Tolerate;
import org.hibernate.envers.Audited;
import org.jbb.lib.db.domain.BaseEntity;

@Getter
@Setter
@Entity
@Audited
@Table(name = "JBB_TOPICS")
@Builder
@EqualsAndHashCode(callSuper = true)
public class TopicEntity extends BaseEntity {

    @NotNull
    @Column(name = "forum_id")
    private Long forumId;

    @Valid
    @NotNull
    @JoinColumn(name = "first_post_id")
    @OneToOne(targetEntity = PostEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PostEntity firstPost;

    @Valid
    @NotNull
    @JoinColumn(name = "last_post_id")
    @OneToOne(targetEntity = PostEntity.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private PostEntity lastPost;

    @Tolerate
    TopicEntity() {
        // for JPA
    }

}
