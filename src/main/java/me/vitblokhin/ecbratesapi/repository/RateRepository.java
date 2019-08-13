package me.vitblokhin.ecbratesapi.repository;

import me.vitblokhin.ecbratesapi.model.Rate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface RateRepository extends JpaRepository<Rate, Long> {
    Optional<Rate> findByDate_DateAndCurrency_CharCode(LocalDate date, String code);
} // interface RateRepository
