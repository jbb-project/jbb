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

import static org.jbb.permissions.api.effective.PermissionVerdict.ALLOW;

import com.google.common.collect.Sets;
import java.util.Set;
import java.util.stream.Collectors;
import javax.cache.annotation.CacheKey;
import javax.cache.annotation.CacheResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.permissions.api.PermissionService;
import org.jbb.permissions.api.effective.EffectivePermission;
import org.jbb.permissions.api.effective.EffectivePermissionTable;
import org.jbb.permissions.api.effective.EffectivePermissionTable.Builder;
import org.jbb.permissions.api.exceptions.PermissionRequiredException;
import org.jbb.permissions.api.identity.MemberIdentity;
import org.jbb.permissions.api.identity.SecurityIdentity;
import org.jbb.permissions.api.permission.PermissionDefinition;
import org.jbb.permissions.api.permission.PermissionType;
import org.jbb.permissions.impl.PermissionCaches;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class DefaultPermissionService implements PermissionService {

    private final UserDetailsSource userDetailsSource;
    private final SecurityIdentityResolver securityIdentityResolver;
    private final EffectivePermissionsBuilder effectivePermissionsBuilder;
    private final ApplicationContext applicationContext;

    @Override
    public EffectivePermissionTable getEffectivePermissionTable(PermissionType permissionType,
        SecurityIdentity securityIdentity) {
        Set<SecurityIdentity> securityIdentities = securityIdentityResolver
            .resolveAffectedIdentities(securityIdentity);

        Builder builder = EffectivePermissionTable.builder();
        effectivePermissionsBuilder.mergePermissions(permissionType, securityIdentities)
            .forEach(builder::putPermission);
        return builder.build();
    }

    @Override
    public boolean checkPermission(PermissionDefinition permissionDefinition,
        SecurityContentUser securityContentUser) {
        boolean hasPermission = getAllAllowedGlobalPermissions(securityContentUser.getUserId())
            .contains(permissionDefinition);
        log.debug("Member with id {} {} permission {}", securityContentUser.getUserId(),
            hasPermission ? "has" : "has NOT", permissionDefinition.getCode());
        return hasPermission;
    }

    @Override
    public boolean checkPermission(PermissionDefinition permissionDefinition) {
        return checkPermission(permissionDefinition, userDetailsSource.getFromApplicationContext());
    }

    @Override
    public void assertPermission(PermissionDefinition permissionDefinition) {
        if (!getSpringProxy().checkPermission(permissionDefinition)) {
            throw new PermissionRequiredException(permissionDefinition);
        }
    }

    @Override
    public Set<PermissionDefinition> getAllAllowedGlobalPermissions(Long memberId) {
        Set<PermissionDefinition> result = Sets.newHashSet();
        result.addAll(getSpringProxy().getAllAllowedAdministratorPermissions(memberId));
        result.addAll(getSpringProxy().getAllAllowedMemberPermissions(memberId));
        return result;
    }

    @Override
    @CacheResult(cacheName = PermissionCaches.ADMINISTRATOR_PERMISSIONS)
    public Set<PermissionDefinition> getAllAllowedAdministratorPermissions(
        @CacheKey Long memberId) {
        return getAllAllowedPermissions(memberId, PermissionType.ADMINISTRATOR_PERMISSIONS);
    }

    @Override
    @CacheResult(cacheName = PermissionCaches.MEMBER_PERMISSIONS)
    public Set<PermissionDefinition> getAllAllowedMemberPermissions(@CacheKey Long memberId) {
        return getAllAllowedPermissions(memberId, PermissionType.MEMBER_PERMISSIONS);
    }

    private Set<PermissionDefinition> getAllAllowedPermissions(Long memberId,
        PermissionType permissionType) {
        Set<EffectivePermission> allPermissions = getEffectivePermissionTable(permissionType,
            new MemberIdentity(memberId)).getPermissions();

        return allPermissions.stream()
            .filter(effectivePermission -> effectivePermission.getVerdict() == ALLOW)
            .map(EffectivePermission::getDefinition).collect(Collectors.toSet());
    }

    private PermissionService getSpringProxy() {
        return applicationContext.getBean(PermissionService.class);
    }
}
