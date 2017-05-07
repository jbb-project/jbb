package org.jbb.system.web.session.form;

import java.time.Duration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionInActiveTimeForm {

    Duration maxInactiveInterval;
}
