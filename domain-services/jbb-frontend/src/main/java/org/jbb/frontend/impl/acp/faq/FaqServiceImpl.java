/*
 * Copyright (C) 2017 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.frontend.impl.acp.faq;


import org.jbb.frontend.api.faq.FaqService;
import org.jbb.frontend.api.faq.model.FaqTuple;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class FaqServiceImpl implements FaqService {

    @Override
    public void save(List<FaqTuple> acpFaqTupleList) {
        throw new UnsupportedOperationException("Todo");
    }

    @Override
    public Map<String, List<FaqTuple>> getFaqDataMap() {
        throw new UnsupportedOperationException("Todo");
    }
}
