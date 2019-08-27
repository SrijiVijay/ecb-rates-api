package me.vitblokhin.ecbratesapi.validation;

import me.vitblokhin.ecbratesapi.repository.CurrencyRepository;
import me.vitblokhin.ecbratesapi.validation.constraints.ValidCurrencyCharCode;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CurrencyCharCodeValidator implements ConstraintValidator<ValidCurrencyCharCode, String> {

    private final CurrencyRepository currencyRepository;

    @Autowired
    public CurrencyCharCodeValidator(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    @Override
    public boolean isValid(String charCode, ConstraintValidatorContext constraintValidatorContext) {
        if (charCode == null || "".equals(charCode)) {
            return true;
        }

        return currencyRepository.findByCharCode(charCode).isPresent();
    }
} // class CurrencyCharCodeValidator
