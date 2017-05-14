package org.jbb.system.web.session.data;

import java.time.Duration;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SessionUITableRow {

    private String id;
    private LocalDateTime creationTime;
    private LocalDateTime lastAccessedTime;
    private Duration usedTime;
    private Duration inactiveTime;
    private String username;
    private String displayName;
    private Duration timeToLive;
}
