package me.vitblokhin.ecbratesapi.validation.constraints;

import me.vitblokhin.ecbratesapi.validation.HistoricalFilterValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({TYPE, ANNOTATION_TYPE})
@Retention(RUNTIME)
@Constraint(validatedBy = {HistoricalFilterValidator.class})
public @interface ValidHistoricalFilter {
    String message() default "Dates must be not null and in past time; endDate must be after startDate in max range of 180 days";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
} // @interface ValidHistoricalFilter
