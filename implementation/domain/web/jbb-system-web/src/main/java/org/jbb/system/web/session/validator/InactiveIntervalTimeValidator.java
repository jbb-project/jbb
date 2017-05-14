package org.jbb.system.web.session.validator;


import org.jbb.system.web.session.form.InactiveIntervalTimeForm;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component("maxinactiveintervaltimevalidator")
public class InactiveIntervalTimeValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return InactiveIntervalTimeForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        InactiveIntervalTimeForm inactiveIntervalTimeForm = (InactiveIntervalTimeForm) o;
        try {
            String maxInactiveInterval = inactiveIntervalTimeForm.getMaxInactiveIntervalTime();
            long parseResult = Long.parseLong(maxInactiveInterval);
            if(parseResult  <=0 ){
                errors.rejectValue("intervalTime","lowValue",new Object[]{"'intervalTime'"},"Provide value greater then 0");
            }

        }
        catch(NumberFormatException ex) {
            errors.rejectValue("intervalTime","notparseValue",new Object[]{"'intervalTime'"},"Value must be able to convert to Long value");
        }
    }
}
