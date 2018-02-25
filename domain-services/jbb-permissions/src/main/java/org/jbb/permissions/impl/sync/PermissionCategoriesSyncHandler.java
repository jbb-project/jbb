/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.sync;

import org.jbb.permissions.api.permission.domain.AllPermissionCategories;
import org.jbb.permissions.impl.acl.dao.AclPermissionCategoryRepository;
import org.jbb.permissions.impl.acl.dao.AclPermissionTypeRepository;
import org.jbb.permissions.impl.acl.model.AclPermissionCategoryEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.util.Arrays;

import lombok.RequiredArgsConstructor;

@Order(3)
@Component
@RequiredArgsConstructor
public class PermissionCategoriesSyncHandler implements SyncHandler {

    private final AclPermissionTypeRepository typeRepository;
    private final AclPermissionCategoryRepository categoryRepository;

    @Override
    public void synchronize() {
        Arrays.stream(AllPermissionCategories.values()).forEach(this::savePermissionCategory);
    }

    private void savePermissionCategory(AllPermissionCategories category) {
        AclPermissionTypeEntity typeEntity = typeRepository.findAllByName(category.getType().name());
        AclPermissionCategoryEntity permissionCategory = categoryRepository
                .findAllByNameAndType(category.getName(), typeEntity);

        if (permissionCategory != null) {
            permissionCategory.setPosition(category.getPosition());
        } else {
            permissionCategory = AclPermissionCategoryEntity.builder()
                    .name(category.getName())
                    .type(typeEntity)
                    .position(category.getPosition())
                    .build();
        }
        categoryRepository.save(permissionCategory);
    }
}
