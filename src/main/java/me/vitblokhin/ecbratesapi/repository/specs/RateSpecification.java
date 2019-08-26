package me.vitblokhin.ecbratesapi.repository.specs;

import me.vitblokhin.ecbratesapi.dto.filter.HistoricalFilter;
import me.vitblokhin.ecbratesapi.dto.filter.SingleDateFilter;
import me.vitblokhin.ecbratesapi.model.Currency;
import me.vitblokhin.ecbratesapi.model.ExchangeDate;
import me.vitblokhin.ecbratesapi.model.Rate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

public abstract class RateSpecification implements Specification<Rate> {

    private RateSpecification() {
    }

    public static RateSpecification build(final SingleDateFilter filter) {
        return new RateSpecification() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Rate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();

                List<String> charCodes = filter.getSymbols();

                if (charCodes != null && !charCodes.isEmpty()) {
                    Join<Rate, Currency> currency = root.join("currency");
                    predicate = criteriaBuilder.and(predicate, currency.get("charCode").in(charCodes));
                }

                LocalDate date = filter.getDate();
                if (date != null) {
                    Join<Rate, ExchangeDate> dateJoin = root.join("date");
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(dateJoin.get("date"), date));
                }

                return predicate;
            }
        };
    }

    public static RateSpecification build(final HistoricalFilter filter) {
        return new RateSpecification() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<Rate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();

                List<String> charCodes = filter.getSymbols();

                if (charCodes != null && !charCodes.isEmpty()) {
                    Join<Rate, Currency> currency = root.join("currency");
                    predicate = criteriaBuilder.and(predicate, currency.get("charCode").in(charCodes));
                }

                LocalDate startDate = filter.getStartDate();
                LocalDate endDate = filter.getEndDate();
                Join<Rate, ExchangeDate> date = root.join("date");
                if (startDate != null && endDate != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(date.get("date"), startDate, endDate));
                }

                return predicate;
            }
        };
    }
} // class RateSpecification
