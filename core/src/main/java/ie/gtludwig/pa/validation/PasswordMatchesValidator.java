package ie.gtludwig.pa.validation;

import ie.gtludwig.pa.controller.dto.UserPojo;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, ConstraintValidatorContext context) {
        final UserPojo user = (UserPojo) obj;
        return user.getPassword().equals(user.getPasswordConfirm());
    }
}
