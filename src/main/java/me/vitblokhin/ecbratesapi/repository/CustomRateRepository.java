package me.vitblokhin.ecbratesapi.repository;

import me.vitblokhin.ecbratesapi.model.Rate;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface CustomRateRepository {
    List<Rate> findAllByDateOrDateBeforeAndCurrencyIn(LocalDate date, List<String> charCodes);
    List<Rate> findAllByDateBetweenAndCurrencyIn(LocalDate dateStart, LocalDate dateEnd, List<String> charCodes);
} // interface CustomRateRepository
