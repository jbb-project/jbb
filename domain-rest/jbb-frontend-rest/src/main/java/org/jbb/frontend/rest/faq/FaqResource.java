/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.rest.faq;

import org.jbb.frontend.api.faq.Faq;
import org.jbb.frontend.api.faq.FaqService;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.permissions.api.annotation.MemberPermissionRequired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

import static org.jbb.frontend.rest.FrontendRestAuthorize.IS_AN_ADMINISTRATOR_OR_OAUTH_FAQ_READ_WRITE_SCOPE;
import static org.jbb.frontend.rest.FrontendRestAuthorize.PERMIT_ALL_OR_OAUTH_FAQ_READ_SCOPE;
import static org.jbb.frontend.rest.FrontendRestConstants.FAQ;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.MISSING_PERMISSION;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.permissions.api.permission.domain.MemberPermissions.CAN_VIEW_FAQ;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + FAQ)
@RequestMapping(value = API_V1 + FAQ, produces = MediaType.APPLICATION_JSON_VALUE)
public class FaqResource {

    private final FaqService faqService;

    private final FaqTranslator faqTranslator;

    @GetMapping
    @ApiOperation("Gets faq")
    @ErrorInfoCodes({MISSING_PERMISSION})
    @MemberPermissionRequired(CAN_VIEW_FAQ)
    @PreAuthorize(PERMIT_ALL_OR_OAUTH_FAQ_READ_SCOPE)
    public FaqDto faqGet() {
        Faq faq = faqService.getFaq();
        return faqTranslator.toDto(faq);
    }

    @PutMapping
    @ApiOperation("Updates faq")
    @ErrorInfoCodes({UNAUTHORIZED, FORBIDDEN})
    @PreAuthorize(IS_AN_ADMINISTRATOR_OR_OAUTH_FAQ_READ_WRITE_SCOPE)
    public FaqDto faqPut(@RequestBody @Validated FaqDto faqDto) {
        faqService.setFaq(faqTranslator.toModel(faqDto));
        return faqDto;
    }

}
