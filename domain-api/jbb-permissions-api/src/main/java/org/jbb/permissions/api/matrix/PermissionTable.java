/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api.matrix;

import com.google.common.collect.Sets;

import org.jbb.permissions.api.entry.PermissionValue;
import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionDefinition;

import java.util.Set;

public class PermissionTable {

    private Set<Permission> permissions;

    private PermissionTable(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public static Builder builder() {
        return new Builder();
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public static class Builder {

        private Set<Permission> permissions = Sets.newHashSet();


        public Builder putPermission(Permission permission) {
            permissions.add(permission);
            return this;
        }

        public Builder putPermission(PermissionDefinition definition, PermissionValue value) {
            permissions.add(new Permission(definition, value));
            return this;
        }

        public Builder removePermission(Permission permission) {
            permissions.remove(permission);
            return this;
        }

        public PermissionTable build() {
            return new PermissionTable(permissions);
        }
    }

}
