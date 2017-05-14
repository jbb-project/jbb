package org.jbb.system.web.session.form;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class InactiveIntervalTimeForm {

    @NotBlank
    @NotNull
    @NotEmpty
    String maxInactiveIntervalTime;
}
