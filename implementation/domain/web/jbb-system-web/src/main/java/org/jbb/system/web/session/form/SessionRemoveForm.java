package org.jbb.system.web.session.form;

import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.NotNull;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SessionRemoveForm {


    @NotBlank
    @NotNull
    @NotEmpty
    String sessionIDToRemove;
}
