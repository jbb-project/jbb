/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.me;

import static org.apache.commons.lang3.StringUtils.SPACE;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = "/api/v1/me", description = SPACE)
@RequestMapping(value = "/api/v1/me", produces = MediaType.APPLICATION_JSON_VALUE)
public class MeResource {

    private final UserDetailsSource userDetailsSource;

    @GetMapping
    @ApiOperation("Gets information about yourself")
    public MemberDto getMe() {

        SecurityContentUser securityContentUser = userDetailsSource.getFromApplicationContext();
        MemberDto memberDto = new MemberDto();
        memberDto.setUsername(securityContentUser.getUsername());
        memberDto.setDisplayedName(securityContentUser.getDisplayedName());
        return memberDto;
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public MemberDto postMe(@RequestBody MemberDto dto) {
        throw new RuntimeException("x");
    }

}
