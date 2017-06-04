/*
 * Copyright (C) 2016 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.members.web.base.logic;

import org.jbb.members.api.base.MemberSearchCriteria;
import org.jbb.members.web.base.data.MemberSearchCriteriaImpl;
import org.jbb.members.web.base.form.SearchMemberForm;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberSearchCriteriaFactoryTest {
    @Test
    public void shouldCreate() throws Exception {
        // when
        MemberSearchCriteriaFactory factory = new MemberSearchCriteriaFactory();
        MemberSearchCriteria searchCriteria = factory.build(new SearchMemberForm());

        // then
        assertThat(searchCriteria).isInstanceOf(MemberSearchCriteriaImpl.class);
    }
}