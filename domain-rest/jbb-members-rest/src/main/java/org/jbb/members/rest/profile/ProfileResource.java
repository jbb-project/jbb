/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.rest.profile;

import static org.apache.commons.lang3.StringUtils.SPACE;
import static org.jbb.lib.restful.RestAuthorize.IS_AUTHENTICATED;
import static org.jbb.lib.restful.RestConstants.API_V1;
import static org.jbb.lib.restful.domain.ErrorInfo.FORBIDDEN;
import static org.jbb.lib.restful.domain.ErrorInfo.GET_NOT_OWN_PROFILE;
import static org.jbb.lib.restful.domain.ErrorInfo.MEMBER_NOT_FOUND;
import static org.jbb.lib.restful.domain.ErrorInfo.UNAUTHORIZED;
import static org.jbb.lib.restful.domain.ErrorInfo.UPDATE_NOT_OWN_PROFILE;
import static org.jbb.members.rest.MembersRestConstants.MEMBERS;
import static org.jbb.members.rest.MembersRestConstants.MEMBER_ID;
import static org.jbb.members.rest.MembersRestConstants.MEMBER_ID_VAR;
import static org.jbb.members.rest.MembersRestConstants.PROFILE;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.jbb.lib.commons.vo.Username;
import org.jbb.lib.restful.domain.ErrorInfoCodes;
import org.jbb.lib.restful.error.ErrorResponse;
import org.jbb.members.api.base.Member;
import org.jbb.members.api.base.MemberService;
import org.jbb.members.api.base.ProfileDataToChange;
import org.jbb.members.api.registration.RegistrationMetaData;
import org.jbb.members.api.registration.RegistrationService;
import org.jbb.members.rest.profile.exception.GetNotOwnProfile;
import org.jbb.members.rest.profile.exception.UpdateNotOwnProfile;
import org.jbb.security.api.role.RoleService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@PreAuthorize(IS_AUTHENTICATED)
@Api(tags = API_V1 + MEMBERS + MEMBER_ID + PROFILE, description = SPACE)
@RequestMapping(value = API_V1 + MEMBERS + MEMBER_ID + PROFILE,
    produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfileResource {

    private final MemberService memberService;
    private final RegistrationService registrationService;
    private final RoleService roleService;

    private final ProfileTranslator profileTranslator;

    @GetMapping
    @ErrorInfoCodes({MEMBER_NOT_FOUND, GET_NOT_OWN_PROFILE, UNAUTHORIZED})
    @ApiOperation("Gets member profile by member id")
    public ProfileDto fullProfileGet(@PathVariable(MEMBER_ID_VAR) Long memberId,
        Authentication authentication) {
        Member member = memberService.getMemberWithId(memberId)
            .orElseThrow(() -> new UsernameNotFoundException(memberId.toString()));
        Member currentMember = getCurrentMember(authentication);
        boolean requestorHasAdminRole = roleService.hasAdministratorRole(currentMember.getId());
        if (!currentMember.getId().equals(memberId) && !requestorHasAdminRole) {
            throw new GetNotOwnProfile();
        }
        RegistrationMetaData registrationMetaData = registrationService
            .getRegistrationMetaData(memberId);
        return profileTranslator.toDto(member, registrationMetaData);
    }

    @PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    @ErrorInfoCodes({MEMBER_NOT_FOUND, UPDATE_NOT_OWN_PROFILE, UNAUTHORIZED, FORBIDDEN})
    @ApiOperation("Updates member profile by member id")
    public ProfileDto profilePut(@PathVariable(MEMBER_ID_VAR) Long memberId,
        @RequestBody UpdateProfileDto updateProfileDto, Authentication authentication) {
        Member currentMember = getCurrentMember(authentication);
        boolean requestorHasAdminRole = roleService.hasAdministratorRole(currentMember.getId());
        if (!currentMember.getId().equals(memberId) && !requestorHasAdminRole) {
            throw new UpdateNotOwnProfile();
        }

        ProfileDataToChange profileDataToChange = profileTranslator.toModel(updateProfileDto);
        memberService.updateProfile(memberId, profileDataToChange);

        return fullProfileGet(memberId, authentication);
    }

    private Member getCurrentMember(Authentication authentication) {
        User currentUser = (User) authentication.getPrincipal();
        Optional<Member> member = memberService.getMemberWithUsername(
            Username.builder().value(currentUser.getUsername()).build());
        if (member.isPresent()) {
            return member.get();
        } else {
            throw new UsernameNotFoundException(currentUser.getUsername());
        }
    }

    @ExceptionHandler(UpdateNotOwnProfile.class)
    public ResponseEntity<ErrorResponse> handle(UpdateNotOwnProfile ex) {
        return ErrorResponse.getErrorResponseEntity(UPDATE_NOT_OWN_PROFILE);
    }

    @ExceptionHandler(GetNotOwnProfile.class)
    public ResponseEntity<ErrorResponse> handle(GetNotOwnProfile ex) {
        return ErrorResponse.getErrorResponseEntity(GET_NOT_OWN_PROFILE);
    }

}
