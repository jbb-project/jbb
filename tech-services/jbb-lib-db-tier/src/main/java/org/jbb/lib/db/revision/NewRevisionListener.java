/*
 * Copyright (C) 2018 the original author or authors.
 *
 * This file is part of jBB Application Project.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  You may obtain a copy of the License at
 *        http://www.apache.org/licenses/LICENSE-2.0
 */

package org.jbb.lib.db.revision;

import org.hibernate.envers.RevisionListener;
import org.jbb.lib.commons.RequestIdUtils;
import org.jbb.lib.commons.security.SecurityContentUser;
import org.jbb.lib.commons.security.UserDetailsSource;
import org.jbb.lib.commons.web.HttpServletRequestHolder;

public class NewRevisionListener implements RevisionListener {
    private final UserDetailsSource userDetailsSource;
    private final HttpServletRequestHolder servletRequestHolder;

    public NewRevisionListener() {
        userDetailsSource = new UserDetailsSource();
        servletRequestHolder = new HttpServletRequestHolder();
    }

    @Override
    public void newRevision(Object revisionEntity) {
        RevisionInfo revEntity = (RevisionInfo) revisionEntity;

        SecurityContentUser securityContentUser = userDetailsSource.getFromApplicationContext();
        if (securityContentUser != null) {
            revEntity.setMemberId(securityContentUser.getUserId());
        }

        revEntity.setIpAddress(servletRequestHolder.getCurrentIpAddress());
        revEntity.setSessionId(servletRequestHolder.getCurrentSessionId());
        revEntity.setRequestId(RequestIdUtils.getCurrentRequestId());
    }
}
