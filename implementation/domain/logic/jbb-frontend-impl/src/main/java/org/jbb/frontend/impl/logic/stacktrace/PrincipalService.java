package org.jbb.frontend.impl.logic.stacktrace;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.security.Principal;

@Component
public class PrincipalService {

    public Principal getPrincipalFromApplicationContext() {
        return (Principal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
