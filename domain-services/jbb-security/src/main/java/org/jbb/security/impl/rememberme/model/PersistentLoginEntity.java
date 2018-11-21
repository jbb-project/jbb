/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.rememberme.model;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
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
@Table(name = "JBB_PERSISTENT_LOGINS")
@Builder
@EqualsAndHashCode(callSuper = true)
public class PersistentLoginEntity extends BaseEntity {

    @NotNull
    @Column(name = "member_id")
    private Long memberId;

    @NotNull
    @Column(name = "series")
    private String series;

    @NotNull
    @Column(name = "token")
    private String token;

    @Column(name = "last_used")
    @NotNull
    private LocalDateTime lastUsed;

    @Tolerate
    PersistentLoginEntity() {
        // for JPA
    }

}
