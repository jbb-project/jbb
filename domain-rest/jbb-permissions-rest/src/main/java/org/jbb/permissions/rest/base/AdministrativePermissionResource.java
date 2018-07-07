/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.permissions.rest.base;

import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.lib.restful.RestAuthorize.IS_AN_ADMINISTRATOR;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.permissions.rest.PermissionsRestConstants.ADMINISTRATIVE_PERMISSIONS;
import static org.jbb.permissions.rest.PermissionsRestConstants.DEFINITIONS;
import static org.jbb.permissions.rest.PermissionsRestConstants.EFFECTIVES;

@RestController
@RequiredArgsConstructor
@PreAuthorize(IS_AN_ADMINISTRATOR)
@Api(tags = API_V1 + ADMINISTRATIVE_PERMISSIONS)
@RequestMapping(value = API_V1 + ADMINISTRATIVE_PERMISSIONS, produces = MediaType.APPLICATION_JSON_VALUE)
public class AdministrativePermissionResource {

    @GetMapping
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Gets administrative permission matrix for identity")
    public PermissionMatrixDto permissionsGet(@Validated @ModelAttribute PermissionIdentityDto permissionIdentity) {
        return PermissionMatrixDto.builder().build();
    }

    @GetMapping(EFFECTIVES)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Gets effective administrative permission matrix for identity")
    public PermissionEffectiveMatrixDto permissionsEffectiveGet(@Validated @ModelAttribute PermissionIdentityDto permissionIdentity) {
        return PermissionEffectiveMatrixDto.builder().build();
    }

    @GetMapping(DEFINITIONS)
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Gets available administrative permission definitions")
    public PermissionCategoriesDto permissionDefinitionsGet() {
        return PermissionCategoriesDto.builder().build();
    }

    @PutMapping
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Updates administrative permission matrix for identity")
    public PermissionMatrixDto permissionsPut(@RequestBody PermissionMatrixDto permissionMatrixDto) {
        return PermissionMatrixDto.builder().build();
    }

}
