/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.impl.userdetails.data;

import org.springframework.security.core.userdetails.User;

import javax.validation.constraints.NotNull;

import lombok.Getter;

public class SecurityContentUser extends User { //NOSONAR
    @Getter
    private String displayedName;

    @Getter
    private Long userId;

    public SecurityContentUser(@NotNull User user, String displayedName, Long id) {
        super(
                user.getUsername(),
                user.getPassword(),
                user.isEnabled(),
                user.isAccountNonExpired(),
                user.isCredentialsNonExpired(),
                user.isAccountNonLocked(),
                user.getAuthorities()
        );
        this.displayedName = displayedName;
        this.userId = id;
    }
}
