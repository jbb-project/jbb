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

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;
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
@Table(name = "JBB_POST_CONTENTS")
@Builder
@EqualsAndHashCode(callSuper = true)
public class PostContentEntity extends BaseEntity {

    @Lob
    private String content;

    @Tolerate
    PostContentEntity() {
        // for JPA
    }

}
