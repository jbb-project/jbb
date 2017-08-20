/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.base;

import java.util.List;
import java.util.Optional;
import org.jbb.lib.commons.vo.Username;
import org.jbb.members.api.registration.MemberRegistrationAware;
import org.springframework.data.domain.Page;

public interface MemberService {

    List<MemberRegistrationAware> getAllMembersSortedByRegistrationDate();

    Optional<Member> getMemberWithId(Long id);

    Optional<Member> getMemberWithUsername(Username username);

    void updateProfile(Long memberId, ProfileDataToChange profileDataToChange);

    void updateAccount(Long memberId, AccountDataToChange accountDataToChange);

    @Deprecated
    List<MemberRegistrationAware> getAllMembersWithCriteria(MemberSearchCriteria criteria);

    Page<MemberRegistrationAware> getAllMembersWithCriteria(MemberCriteria criteria);

    void removeMember(Long memberId);
}
