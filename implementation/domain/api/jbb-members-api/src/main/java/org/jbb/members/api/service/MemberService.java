/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.api.service;

import org.jbb.lib.core.vo.Username;
import org.jbb.members.api.data.AccountDataToChange;
import org.jbb.members.api.data.Member;
import org.jbb.members.api.data.MemberRegistrationAware;
import org.jbb.members.api.data.MemberSearchCriteria;
import org.jbb.members.api.data.ProfileDataToChange;

import java.util.List;
import java.util.Optional;

public interface MemberService {

    List<MemberRegistrationAware> getAllMembersSortedByRegistrationDate();

    Optional<Member> getMemberWithUsername(Username username);

    void updateProfile(Username username, ProfileDataToChange profileDataToChange);

    void updateAccount(Username username, AccountDataToChange accountDataToChange);

    List<MemberRegistrationAware> getAllMembersWithCriteria(MemberSearchCriteria criteria);
}
