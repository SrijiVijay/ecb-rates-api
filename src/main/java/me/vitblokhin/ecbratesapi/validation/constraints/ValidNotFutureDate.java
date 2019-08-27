package me.vitblokhin.ecbratesapi.validation.constraints;

import me.vitblokhin.ecbratesapi.validation.NotFutureDateValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NotFutureDateValidator.class)
public @interface ValidNotFutureDate {
    String message() default "date must be not null and past or current";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
} // @interface ValidNotFutureDate
