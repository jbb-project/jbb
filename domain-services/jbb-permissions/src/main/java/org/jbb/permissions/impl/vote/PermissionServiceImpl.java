/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.impl.vote;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.effective.EffectivePermissionTable;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.jbb.permissions.api.permission.PermissionType;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final UserDetailsSource userDetailsSource;
    private final SecurityIdentityResolver securityIdentityResolver;

    @Override
    public EffectivePermissionTable getEffectivePermissionTable(PermissionType permissionType,
        SecurityIdentity securityIdentity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean checkPermission(PermissionDefinition permissionDefinition,
        SecurityContentUser securityContentUser) {
        return getAllAllowedGlobalPermissions(securityContentUser.getUserId())
            .contains(permissionDefinition);
    }

    @Override
    public boolean checkPermission(PermissionDefinition permissionDefinition) {
        return checkPermission(permissionDefinition, userDetailsSource.getFromApplicationContext());
    }

    @Override
    public Set<PermissionDefinition> getAllAllowedGlobalPermissions(Long memberId) {
        Set<PermissionDefinition> result = Sets.newHashSet();
        result.addAll(getAllAllowedAdministratorPermissions(memberId));
        result.addAll(getAllAllowedMemberPermissions(memberId));
        return result;
    }

    @Override//TODO caching
    public Set<PermissionDefinition> getAllAllowedAdministratorPermissions(Long memberId) {
        return getAllAllowedPermissions(memberId, PermissionType.ADMINISTRATOR_PERMISSIONS);
    }

    @Override//TODO caching
    public Set<PermissionDefinition> getAllAllowedMemberPermissions(Long memberId) {
        return getAllAllowedPermissions(memberId, PermissionType.MEMBER_PERMISSIONS);
    }

    private Set<PermissionDefinition> getAllAllowedPermissions(Long memberId,
        PermissionType permissionType) {
        Set<SecurityIdentity> securityIdentities = securityIdentityResolver
            .resolveIdentities(memberId);

        Set<PermissionDefinition> result = Sets.newHashSet();
        Set<EffectivePermissionTable> collect = securityIdentities.stream()
            .map(securityIdentity -> getEffectivePermissionTable(permissionType, securityIdentity))
            .collect(Collectors.toSet());

        return result;
    }
}
