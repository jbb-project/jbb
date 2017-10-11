/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.registration;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.members.rest.MembersRestConstants.MEMBERS;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Set;
import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.vo.IPAddress;
import org.jbb.lib.restful.domain.ErrorInfo;
import org.jbb.lib.restful.error.ErrorDetail;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.registration.RegistrationException;
import org.jbb.members.api.registration.RegistrationRequest;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.rest.base.MemberDto;
import org.jbb.members.rest.base.MemberTranslator;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Api(tags = API_V1 + MEMBERS, description = SPACE)
@RequestMapping(value = API_V1 + MEMBERS, produces = MediaType.APPLICATION_JSON_VALUE)
public class MemberRegistrationResource {

    private final RegistrationService registrationService;
    private final MemberService memberService;

    private final RegistrationRequestTranslator requestTranslator;
    private final MemberTranslator memberTranslator;

    @ResponseStatus(HttpStatus.CREATED)
    @ApiOperation("Creates new member")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public MemberDto membersPost(@RequestBody RegistrationRequestDto registrationDto,
        HttpServletRequest httpServletRequest) {
        IPAddress ipAddress = IPAddress.builder().value(httpServletRequest.getRemoteAddr()).build();
        RegistrationRequest registrationRequest = requestTranslator
            .toModel(registrationDto, ipAddress);
        registrationService.register(registrationRequest);
        Member newCreatedMember = memberService
            .getMemberWithUsername(registrationRequest.getUsername())
            .orElseThrow(IllegalStateException::new);
        return memberTranslator.toDto(newCreatedMember);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<ErrorResponse> handle(RegistrationException ex) {
        ErrorResponse errorResponse = ErrorResponse.createFrom(ErrorInfo.REGISTRATION_FAILED);
        Set<ConstraintViolation<?>> constraintViolations = ex.getConstraintViolations();

        constraintViolations.stream()
            .map(violation -> new ErrorDetail(violation.getPropertyPath().toString(),
                violation.getMessage()))
            .forEach(errorDetail -> errorResponse.getDetails().add(errorDetail));

        return new ResponseEntity<>(errorResponse, errorResponse.getStatus());
    }

}
