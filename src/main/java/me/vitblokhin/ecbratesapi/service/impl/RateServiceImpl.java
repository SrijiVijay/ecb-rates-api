package me.vitblokhin.ecbratesapi.service.impl;

import lombok.extern.log4j.Log4j2;
import me.vitblokhin.ecbratesapi.client.response.DailyData;
import me.vitblokhin.ecbratesapi.client.response.Envelope;
import me.vitblokhin.ecbratesapi.client.response.RateData;
import me.vitblokhin.ecbratesapi.dto.filter.QueryFilter;
import me.vitblokhin.ecbratesapi.dto.json.DailyRateDto;
import me.vitblokhin.ecbratesapi.exception.ItemNotFoundException;
import me.vitblokhin.ecbratesapi.model.Currency;
import me.vitblokhin.ecbratesapi.model.ExchangeDate;
import me.vitblokhin.ecbratesapi.model.Rate;
import me.vitblokhin.ecbratesapi.repository.CurrencyRepository;
import me.vitblokhin.ecbratesapi.repository.ExchangeDateRepository;
import me.vitblokhin.ecbratesapi.repository.RateRepository;
import me.vitblokhin.ecbratesapi.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Log4j2
@Service("rateService")
public class RateServiceImpl implements RateService {

    private final RateRepository rateRepository;
    private final CurrencyRepository currencyRepository;
    private final ExchangeDateRepository exchangeDateRepository;

    @Autowired
    public RateServiceImpl(RateRepository rateRepository,
                           CurrencyRepository currencyRepository,
                           ExchangeDateRepository exchangeDateRepository) {
        this.rateRepository = rateRepository;
        this.currencyRepository = currencyRepository;
        this.exchangeDateRepository = exchangeDateRepository;
    }

    @Override
    public DailyRateDto getDailyRate(QueryFilter filter, LocalDate date) {
        DailyRateDto dailyRates = new DailyRateDto();

        ExchangeDate exDate = exchangeDateRepository.findByDate(date)
                .orElse(exchangeDateRepository.findFirstByDateBeforeOrderByDateDesc(date)
                        .orElseThrow(() -> new ItemNotFoundException("Rates not found")));

        dailyRates.setDate(exDate.getDate());

        Map<String, BigDecimal> rateMap = new HashMap<>();
        String base = filter.getBase();
        List<String> symbols =
                (Objects.isNull(filter.getSymbols()) ||
                        filter.getSymbols().isEmpty()) ? null
                        : filter.getSymbols();

        if (base == null || "EUR".equals(base) || "".equals(base)) {
            dailyRates.setBase("EUR");
            for (Rate rate : exDate.getRates()) {
                String code = rate.getCurrency().getCharCode();
                if(Objects.isNull(symbols) || symbols.contains(code)) {
                    rateMap.put(code, rate.getRate());
                }
            }
        } else {
            Rate baseRate = exDate.getRates()
                    .stream()
                    .filter(r -> r.getCurrency()
                            .getCharCode()
                            .equals(base))
                    .findFirst()
                    .orElseThrow(() -> new ItemNotFoundException("Selected base not found"));

            dailyRates.setBase(baseRate.getCurrency().getCharCode());

            for (Rate rate : exDate.getRates()) {
                String code = rate.getCurrency().getCharCode();
                BigDecimal tmpRate = rate.getRate();
                if(code.equals(base)){
                    code = "EUR";
                    tmpRate = BigDecimal.valueOf(1);
                }
                if(Objects.isNull(symbols) || symbols.contains(code)) {
                    tmpRate = tmpRate.divide(baseRate.getRate(), rate.getRate().scale(), RoundingMode.HALF_UP);
                    rateMap.put(code, tmpRate);
                }
            }
        }

        dailyRates.setRates(rateMap);
        return dailyRates;
    }

    @Override
    public void updateRatesData(Envelope envelope) {
        for (DailyData dailyData : envelope.getCube().getData()) {
            if (!exchangeDateRepository.findByDate(dailyData.getTime()).isPresent()) {
                this.saveDailyRates(dailyData);
            }
        }
    }

    @Transactional
    void saveDailyRates(DailyData dailyData) {
        List<Rate> initRates = new ArrayList<>();
        LocalDate date = dailyData.getTime();
        ExchangeDate exDate = this.prepareExchangeDate(date);
        for (RateData rateData : dailyData.getRates()) {
            String code = rateData.getCurrency();
            Rate rate = new Rate();
            Currency currency = this.prepareCurrency(code);

            //if(!rateRepository.findByDate_DateAndCurrency_CharCode(date, code).isPresent()) {

                rate.setRate(rateData.getRate());
                rate.setCurrency(currency);
                rate.setDate(exDate);
                exDate.getRates().add(rate);

                initRates.add(rate);
            //}
        }
        log.info("saving rates for date: {}", date);
        rateRepository.saveAll(initRates);
    }

    @Transactional
    ExchangeDate prepareExchangeDate(LocalDate date) {
        Optional<ExchangeDate> optExDate = exchangeDateRepository.findByDate(date);
        if (!optExDate.isPresent()) {
            ExchangeDate exDate = new ExchangeDate();
            exDate.setDate(date);
            exDate.setRates(new ArrayList<>());
            return exchangeDateRepository.save(exDate);
        } else {
            return optExDate.get();
        }
    }

    @Transactional
    Currency prepareCurrency(String charCode) {
        Optional<Currency> optCurrency = currencyRepository.findByCharCode(charCode);
        if (!optCurrency.isPresent()) {
            Currency currency = new Currency();
            currency.setCharCode(charCode);
            return currencyRepository.save(currency);
        } else {
            return optCurrency.get();
        }
    }
} // class RateServiceImpl
