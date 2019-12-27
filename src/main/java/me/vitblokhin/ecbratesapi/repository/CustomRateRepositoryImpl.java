package me.vitblokhin.ecbratesapi.repository;

import me.vitblokhin.ecbratesapi.model.Currency;
import me.vitblokhin.ecbratesapi.model.ExchangeDate;
import me.vitblokhin.ecbratesapi.model.Rate;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.List;

public class CustomRateRepositoryImpl implements CustomRateRepository {

    private final EntityManager em;

    @Autowired
    public CustomRateRepositoryImpl(EntityManager entityManager) {
        this.em = entityManager;
    }

    @Override
    public List<Rate> findAllByDateOrDateBeforeAndCurrencyIn(LocalDate date, List<String> charCodes) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rate> cq = cb.createQuery(Rate.class);

        Root<Rate> root = cq.from(Rate.class);
        Join<Rate, ExchangeDate> dateJoin = root.join("date");

        Subquery<LocalDate> subQuery = cq.subquery(LocalDate.class);
        Root<ExchangeDate> subRoot = subQuery.from(ExchangeDate.class);
        Expression<LocalDate> exp = subRoot.get("date");
        subQuery.where(cb.lessThanOrEqualTo(subRoot.get("date"), date));
        subQuery.select(cb.greatest(exp)).where(cb.lessThanOrEqualTo(subRoot.get("date"), date));

        Predicate predicate = cb.equal(dateJoin.get("date"), subQuery);

        if(charCodes != null && !charCodes.isEmpty()) {
            Join<Rate, Currency> currency = root.join("currency");
            predicate = cb.and(predicate, currency.get("charCode").in(charCodes));
        }

        cq.select(root);
        cq.where(predicate);

        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Rate> findAllByDateBetweenAndCurrencyIn(LocalDate dateStart, LocalDate dateEnd, List<String> charCodes) {

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Rate> cq = cb.createQuery(Rate.class);

        Root<Rate> root = cq.from(Rate.class);
        Join<Rate, ExchangeDate> dateJoin = root.join("date");

        Predicate predicate = cb.between(dateJoin.get("date"), dateStart, dateEnd);

        if(charCodes != null && !charCodes.isEmpty()) {
            Join<Rate, Currency> currency = root.join("currency");
            predicate = cb.and(predicate, currency.get("charCode").in(charCodes));
        }

        cq.select(root);
        cq.where(predicate);

        return em.createQuery(cq).getResultList();
    }
} // class CustomRateRepositoryImpl
