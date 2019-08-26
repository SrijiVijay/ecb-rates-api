package me.vitblokhin.ecbratesapi.repository.specs;

import me.vitblokhin.ecbratesapi.dto.filter.HistoricalFilter;
import me.vitblokhin.ecbratesapi.dto.filter.SingleDateFilter;
import me.vitblokhin.ecbratesapi.model.Currency;
import me.vitblokhin.ecbratesapi.model.ExchangeDate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;

import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

public abstract class ExchangeDateSpecification implements Specification<ExchangeDate> {

    private ExchangeDateSpecification() {
    }

    public static ExchangeDateSpecification build(final SingleDateFilter filter) {
        return new ExchangeDateSpecification() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<ExchangeDate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                List<String> charCodes = filter.getSymbols();

                if (charCodes != null && !charCodes.isEmpty()) {
                    Join<ExchangeDate, Currency> currency = root.joinList("rates").join("currency");
                    predicate = criteriaBuilder.and(predicate, currency.get("charCode").in(charCodes));
                }

                LocalDate date = filter.getDate();
                if (date != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("date"), filter.getDate()));
                }

                return predicate;
            }
        };
    }

    public static ExchangeDateSpecification build(final HistoricalFilter filter) {
        return new ExchangeDateSpecification() {
            @Nullable
            @Override
            public Predicate toPredicate(Root<ExchangeDate> root, CriteriaQuery<?> criteriaQuery, CriteriaBuilder criteriaBuilder) {
                Predicate predicate = criteriaBuilder.conjunction();
                List<String> charCodes = filter.getSymbols();

                if (charCodes != null && !charCodes.isEmpty()) {
                    Join<ExchangeDate, Currency> currency = root.joinList("rates").join("currency");
                    predicate = criteriaBuilder.and(predicate, currency.get("charCode").in(charCodes));
                }

                LocalDate startDate = filter.getStartDate();
                LocalDate endDate = filter.getEndDate();
                if (startDate != null && endDate != null) {
                    predicate = criteriaBuilder.and(predicate, criteriaBuilder.between(root.get("date"), startDate, endDate));
                }

                return predicate;
            }
        };
    }

} // class ExchangeDateSpecification
