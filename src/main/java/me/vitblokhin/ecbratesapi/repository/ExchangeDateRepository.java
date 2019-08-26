package me.vitblokhin.ecbratesapi.repository;

import me.vitblokhin.ecbratesapi.model.ExchangeDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.time.LocalDate;
import java.util.Optional;

public interface ExchangeDateRepository extends JpaRepository<ExchangeDate, Long>, JpaSpecificationExecutor<ExchangeDate> {
    Optional<ExchangeDate> findByDate(LocalDate date);
    Optional<ExchangeDate> findFirstByDateBeforeOrderByDateDesc(LocalDate date);
} // interface ExchangeDateRepository
