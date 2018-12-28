/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.rest.role;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.permissions.rest.PermissionsRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_ADMINISTRATIVE_ROLE_READ_SCOPE;
import static org.jbb.permissions.rest.PermissionsRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_ADMINISTRATIVE_ROLE_READ_WRITE_SCOPE;
import static org.jbb.permissions.rest.PermissionsRestConstants.ADMINISTRATIVE_PERMISSIONS;
import static org.jbb.permissions.rest.PermissionsRestConstants.POSITION;
import static org.jbb.permissions.rest.PermissionsRestConstants.ROLES;
import static org.jbb.permissions.rest.PermissionsRestConstants.ROLE_ID;
import static org.jbb.permissions.rest.PermissionsRestConstants.ROLE_ID_VAR;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + ADMINISTRATIVE_PERMISSIONS + ROLES)
@RequestMapping(value = API_V1 + ADMINISTRATIVE_PERMISSIONS + ROLES, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministrativePermissionRoleResource {

    @GetMapping
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Gets administrative permission roles")
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_ADMINISTRATIVE_ROLE_READ_SCOPE)
    public PermissionRolesDto rolesGet(@Validated @ModelAttribute RoleCriteriaDto roleCriteria) {
        return PermissionRolesDto.builder().build();
    }

    @GetMapping(ROLE_ID)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Gets administrative permission role by id")
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_ADMINISTRATIVE_ROLE_READ_SCOPE)
    public PermissionRoleDto roleGet(@PathVariable(ROLE_ID_VAR) Long roleId) {
        return PermissionRoleDto.builder().build();
    }

    @PostMapping
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Creates administrative permission role")
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_ADMINISTRATIVE_ROLE_READ_WRITE_SCOPE)
    public PermissionRoleDto rolePost(@RequestBody CreateUpdatePermissionRoleDto permissionRoleDto) {
        return PermissionRoleDto.builder().build();
    }

    @PutMapping(ROLE_ID)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Updates administrative permission role by id")
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_ADMINISTRATIVE_ROLE_READ_WRITE_SCOPE)
    public PermissionRoleDto rolePut(@PathVariable(ROLE_ID_VAR) Long roleId,
                                     @RequestBody CreateUpdatePermissionRoleDto permissionRoleDto) {
        return PermissionRoleDto.builder().build();
    }

    @PutMapping(ROLE_ID + POSITION)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Updates position of administrative permission role by id")
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_ADMINISTRATIVE_ROLE_READ_WRITE_SCOPE)
    public PermissionRoleDto rolePositionPut(@PathVariable(ROLE_ID_VAR) Long roleId,
                                             @RequestBody RolePositionDto rolePosition) {
        return PermissionRoleDto.builder().build();
    }

    @DeleteMapping(ROLE_ID)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation("Removes administrative permission role by id")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_PERMISSION_ADMINISTRATIVE_ROLE_READ_WRITE_SCOPE)
    public void roleDelete(@PathVariable(ROLE_ID_VAR) Long roleId) {

    }

}
