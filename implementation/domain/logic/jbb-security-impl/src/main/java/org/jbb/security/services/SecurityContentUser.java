/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.security.services;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class SecurityContentUser extends User {
    private String displayedName;

    public SecurityContentUser(String username, String password, boolean enabled,
                               boolean accountNonExpired, boolean credentialsNonExpired,
                               boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    public String getDisplayedName() {
        return displayedName;
    }

    void setDisplayedName(String displayedName) {
        this.displayedName = displayedName;
    }


}
