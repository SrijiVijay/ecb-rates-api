package me.vitblokhin.ecbratesapi.validation;

import me.vitblokhin.ecbratesapi.validation.constraints.ValidNotFutureDate;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class NotFutureDateValidator implements ConstraintValidator<ValidNotFutureDate, LocalDate> {
    @Override
    public boolean isValid(LocalDate date, ConstraintValidatorContext constraintValidatorContext) {
        if(date == null) {
            return false;
        }
        LocalDate now = LocalDate.now();
        return date.isBefore(now) || date.isEqual(now);
    }
} // class PastDateValidator
