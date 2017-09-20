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

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import org.jbb.permissions.api.permission.Permission;
import org.jbb.permissions.api.permission.PermissionCategory;

public class PermissionTable {

    private Multimap<PermissionCategory, Permission> permissionMultimap = ArrayListMultimap
        .create();

    public List<PermissionCategory> getCategories() {
        return permissionMultimap.keySet().stream()
            .sorted(Comparator.comparingInt(PermissionCategory::getPosition))
            .collect(Collectors.toList());
    }

    public List<Permission> getPermissions(PermissionCategory category) {
        return permissionMultimap.get(category).stream()
            .sorted(Comparator.comparingInt(per -> per.getDefinition().getPosition()))
            .collect(Collectors.toList());
    }

}
