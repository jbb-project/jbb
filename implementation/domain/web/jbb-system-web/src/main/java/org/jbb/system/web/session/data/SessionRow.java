package org.jbb.system.web.session.data;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class SessionRow {

    private String id;
    private LocalDateTime creationTime;
    private LocalDateTime lastAccessedTime;
    private Duration usedTime;
    private Duration inactiveTime;
    private String username;
    private String displayName;
    private Duration timeToLive;
}
