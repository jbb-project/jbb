/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.acl;

import java.util.Arrays;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.jbb.permissions.api.identity.AdministratorGroupIdentity;
import org.jbb.permissions.api.identity.AllMembersIdentity;
import org.jbb.permissions.api.identity.AnonymousIdentity;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.identity.SecurityIdentity.Type;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.api.permission.domain.AdministratorPermissions;
import org.jbb.permissions.api.permission.domain.AllPermissionCategories;
import org.jbb.permissions.api.permission.domain.MemberPermissions;
import org.jbb.permissions.impl.acl.dao.AclPermissionCategoryRepository;
import org.jbb.permissions.impl.acl.dao.AclPermissionRepository;
import org.jbb.permissions.impl.acl.dao.AclPermissionTypeRepository;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityRepository;
import org.jbb.permissions.impl.acl.dao.AclSecurityIdentityTypeRepository;
import org.jbb.permissions.impl.acl.model.AclPermissionCategoryEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionEntity;
import org.jbb.permissions.impl.acl.model.AclPermissionTypeEntity;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityEntity;
import org.jbb.permissions.impl.acl.model.AclSecurityIdentityTypeEntity;
import org.springframework.stereotype.Component;

@Component("AclInstallationAction")
@RequiredArgsConstructor
public class AclInstallationAction {

    private final AclSecurityIdentityTypeRepository aclSecurityIdentityTypeRepository;
    private final AclSecurityIdentityRepository aclSecurityIdentityRepository;

    private final AclPermissionTypeRepository aclPermissionTypeRepository;
    private final AclPermissionCategoryRepository aclPermissionCategoryRepository;
    private final AclPermissionRepository aclPermissionRepository;

    @PostConstruct
    public void create() {
        Arrays.stream(Type.values()).forEach(this::saveIdentityType);
        Arrays.stream(PermissionType.values()).forEach(this::savePermissionType);
        Arrays.stream(AllPermissionCategories.values()).forEach(this::savePermissionCategory);

        Arrays.stream(AdministratorPermissions.values()).forEach(this::savePermission);
        Arrays.stream(MemberPermissions.values()).forEach(this::savePermission);

        createIdentity(AnonymousIdentity.getInstance());
        createIdentity(AdministratorGroupIdentity.getInstance());
        createIdentity(AllMembersIdentity.getInstance());
    }

    private void saveIdentityType(Type type) {
        AclSecurityIdentityTypeEntity identityType = AclSecurityIdentityTypeEntity.builder()
            .name(type.name())
            .build();
        aclSecurityIdentityTypeRepository.save(identityType);
    }

    private void savePermissionType(PermissionType type) {
        AclPermissionTypeEntity permissionType = AclPermissionTypeEntity.builder()
            .name(type.name())
            .build();
        aclPermissionTypeRepository.save(permissionType);
    }

    private void savePermissionCategory(AllPermissionCategories category) {
        AclPermissionCategoryEntity permissionCategory = AclPermissionCategoryEntity.builder()
            .name(category.getName())
            .type(aclPermissionTypeRepository.findAllByName(category.getType().name()))
            .position(category.getPosition())
            .build();
        aclPermissionCategoryRepository.save(permissionCategory);
    }

    private void savePermission(PermissionDefinition permissionDefinition) {
        AclPermissionEntity permission = AclPermissionEntity.builder()
            .name(permissionDefinition.getName())
            .code(permissionDefinition.getCode())
            .category(aclPermissionCategoryRepository
                .findAllByName(permissionDefinition.getCategory().getName()))
            .position(permissionDefinition.getPosition())
            .build();
        aclPermissionRepository.save(permission);
    }

    private void createIdentity(SecurityIdentity securityIdentity) {
        AclSecurityIdentityEntity identity = AclSecurityIdentityEntity.builder()
            .type(
                aclSecurityIdentityTypeRepository.findAllByName(securityIdentity.getType().name()))
            .primarySid(securityIdentity.getId())
            .build();
        aclSecurityIdentityRepository.save(identity);
    }

}
