/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.services;


import org.jbb.lib.core.vo.Login;
import org.jbb.members.api.model.Member;
import org.jbb.members.api.model.MemberRegistrationAware;
import org.jbb.members.api.services.MemberService;
import org.jbb.members.dao.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Autowired
    public MemberServiceImpl(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<MemberRegistrationAware> getAllMembersSortedByRegistrationDate() {
        return memberRepository.findAllByOrderByRegistrationDateAsc()
                .stream()
                .map(memberEntity -> (MemberRegistrationAware) memberEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<? extends Member> getMemberWIthLogin(Login login) {
        return memberRepository.findByLogin(login);
    }

}
