package me.vitblokhin.ecbratesapi.repository;

import me.vitblokhin.ecbratesapi.model.ExchangeDate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeDateRepository extends JpaRepository<ExchangeDate, Long> {
    Optional<ExchangeDate> findByDate(LocalDate date);
} // interface ExchangeDateRepository
