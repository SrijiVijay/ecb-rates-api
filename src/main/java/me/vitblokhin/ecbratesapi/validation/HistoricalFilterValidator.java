package me.vitblokhin.ecbratesapi.validation;

import me.vitblokhin.ecbratesapi.dto.filter.HistoricalFilter;
import me.vitblokhin.ecbratesapi.validation.constraints.ValidHistoricalFilter;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class HistoricalFilterValidator implements ConstraintValidator<ValidHistoricalFilter, HistoricalFilter> {

    @Override
    public boolean isValid(HistoricalFilter filter, ConstraintValidatorContext constraintValidatorContext) {
        if (filter == null) {
            return true;
        }

        LocalDate startDate = filter.getStartDate();
        LocalDate endDate = filter.getEndDate();

        if (startDate == null || endDate == null) {
            return false;
        }

        if (endDate.isBefore(LocalDate.now()) || endDate.isEqual(LocalDate.now())) {
            long daysDiff = startDate.until(endDate, ChronoUnit.DAYS);
            return daysDiff > 0 && daysDiff <=180;
        }

        return false;
    }
} // class HistoricalFilterValidator
