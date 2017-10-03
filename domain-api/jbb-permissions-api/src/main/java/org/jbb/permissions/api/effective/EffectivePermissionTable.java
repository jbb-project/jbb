/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.api.effective;

import com.google.common.collect.Sets;
import java.util.Set;
import org.jbb.permissions.api.permission.PermissionDefinition;

public class EffectivePermissionTable {

    private Set<EffectivePermission> permissions;

    private EffectivePermissionTable(Set<EffectivePermission> permissions) {
        this.permissions = permissions;
    }

    public static EffectivePermissionTable.Builder builder() {
        return new EffectivePermissionTable.Builder();
    }

    public Set<EffectivePermission> getPermissions() {
        return permissions;
    }

    public static class Builder {

        private Set<EffectivePermission> permissions = Sets.newHashSet();


        public EffectivePermissionTable.Builder putPermission(EffectivePermission permission) {
            permissions.add(permission);
            return this;
        }

        public EffectivePermissionTable.Builder putPermission(PermissionDefinition definition,
            PermissionVerdict verdict) {
            permissions.add(new EffectivePermission(definition, verdict));
            return this;
        }

        public EffectivePermissionTable.Builder removePermission(EffectivePermission permission) {
            permissions.remove(permission);
            return this;
        }

        public EffectivePermissionTable build() {
            return new EffectivePermissionTable(permissions);
        }
    }

}
