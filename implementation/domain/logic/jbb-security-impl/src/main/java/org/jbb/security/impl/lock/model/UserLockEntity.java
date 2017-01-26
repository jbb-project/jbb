/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.lock.model;


import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Tolerate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "JBB_USER_LOCKS")
@Builder
public class UserLockEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private Long memberID;

    @Column(name = "user_account_blockade_expire_date")
    @NotNull
    private LocalDateTime accountExpireDate;

    @Tolerate
    UserLockEntity() {
        memberID = -1L;
        accountExpireDate = LocalDateTime.now();
    }


}
