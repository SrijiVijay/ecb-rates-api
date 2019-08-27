package me.vitblokhin.ecbratesapi.validation.constraints;

import me.vitblokhin.ecbratesapi.validation.CurrencyCharCodeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CurrencyCharCodeValidator.class)
public @interface ValidCurrencyCharCode {
    String message() default "Specified base currency does not exist";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
} // @interface ValidCurrencyCharCode
