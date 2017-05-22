package org.jbb.system.web.session.form;

import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InactiveIntervalTimeForm {

    @NotNull
    @Min(0)
    @Digits(integer = 6,fraction = 0)
    Long maxInactiveIntervalTime;
}
