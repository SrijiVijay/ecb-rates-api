package me.vitblokhin.ecbratesapi.service.impl;

import lombok.extern.log4j.Log4j2;
import me.vitblokhin.ecbratesapi.client.response.DailyData;
import me.vitblokhin.ecbratesapi.client.response.Envelope;
import me.vitblokhin.ecbratesapi.client.response.RateData;
import me.vitblokhin.ecbratesapi.dto.filter.BasicFilter;
import me.vitblokhin.ecbratesapi.dto.filter.HistoricalFilter;
import me.vitblokhin.ecbratesapi.dto.filter.SingleDateFilter;
import me.vitblokhin.ecbratesapi.dto.json.DailyRateDto;
import me.vitblokhin.ecbratesapi.dto.json.HistoricalRateDto;
import me.vitblokhin.ecbratesapi.exception.ItemNotFoundException;
import me.vitblokhin.ecbratesapi.model.Currency;
import me.vitblokhin.ecbratesapi.model.ExchangeDate;
import me.vitblokhin.ecbratesapi.model.Rate;
import me.vitblokhin.ecbratesapi.repository.CurrencyRepository;
import me.vitblokhin.ecbratesapi.repository.ExchangeDateRepository;
import me.vitblokhin.ecbratesapi.repository.RateRepository;
import me.vitblokhin.ecbratesapi.repository.specs.RateSpecification;
import me.vitblokhin.ecbratesapi.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;

@Log4j2
@Service("rateService")
public class RateServiceImpl implements RateService {
    @Value("${currency.base}")
    private String BASE_CURRENCY_CHAR;

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
    public DailyRateDto getDailyRate(SingleDateFilter filter) {
        log.info(filter);
        filter = prepareFilter(filter);
        DailyRateDto result = new DailyRateDto();

        Map<String, BigDecimal> rateMap = new HashMap<>();
        final String base = filter.getBase();
        List<String> charCodes = filter.getSymbols();

        ExchangeDate exDate = exchangeDateRepository.findByDate(filter.getDate())
                .orElse(exchangeDateRepository.findFirstByDateBeforeOrderByDateDesc(filter.getDate())
                        .orElseThrow(() -> new ItemNotFoundException("Rates not found")));
        List<Rate> rates = exDate.getRates();

        result.setDate(exDate.getDate());

        Rate baseRate = null;

        for (Rate rate : rates) {
            String charCode = rate.getCurrency().getCharCode();
            if (charCodes == null || charCodes.contains(charCode)) {
                BigDecimal rateValue = rate.getRate();

                rateMap.put(charCode, rateValue);

                if (charCode.equals(base)) {
                    baseRate = rate;
                }
            }
        }

        if (baseRate != null) {
            final Rate br = baseRate;
            rateMap.replaceAll((c, r) ->
                    Objects.equals(c, br.getCurrency().getCharCode()) ?
                            BigDecimal.valueOf(1) :
                            r.divide(br.getRate(), r.scale(), RoundingMode.HALF_UP)
            );
            if (charCodes != null && charCodes.contains(BASE_CURRENCY_CHAR)) {
                rateMap.computeIfAbsent(BASE_CURRENCY_CHAR, (c) ->
                        BigDecimal.valueOf(1)
                                .divide(br.getRate(), br.getRate().scale(), RoundingMode.HALF_UP)
                );
            }

        } else if (!BASE_CURRENCY_CHAR.equals(base)) {
            throw new ItemNotFoundException("Rates for selected base currency " + base + " and date " + filter.getDate() +" not found");
        }

        result.setBase(base);
        result.setRates(rateMap);
        return result;
    }

    @Override
    public HistoricalRateDto getHistoricalRate(HistoricalFilter filter) {
        log.info(filter);
        filter = prepareFilter(filter);

        HistoricalRateDto result = new HistoricalRateDto();

        String base = filter.getBase();
        List<String> charCodes = filter.getSymbols();

        List<Rate> rates = rateRepository.findAll(RateSpecification.build(filter));

        Map<LocalDate, Map<String, BigDecimal>> dateMap = new HashMap<>();
        List<Rate> baseRates = new ArrayList<>();

        for (Rate rate : rates) {
            LocalDate date = rate.getDate().getDate();
            String charCode = rate.getCurrency().getCharCode();
            BigDecimal rateValue = rate.getRate();

            Map<String, BigDecimal> rateMap = dateMap.getOrDefault(date, new HashMap<>());
            rateMap.put(charCode, rateValue);
            dateMap.putIfAbsent(date, rateMap);

            if (charCode.equals(base)) {
                baseRates.add(rate);
            }
        }

        if (!baseRates.isEmpty()) {
            for (Rate baseRate : baseRates) {
                Map<String, BigDecimal> map =
                        dateMap.get(baseRate.getDate().getDate());
                map.replaceAll((c, r) ->
                        Objects.equals(c, baseRate.getCurrency().getCharCode()) ?
                                BigDecimal.valueOf(1) :
                                r.divide(baseRate.getRate(), r.scale(), RoundingMode.HALF_UP)
                );
                if (charCodes != null && charCodes.contains(BASE_CURRENCY_CHAR)) {
                    map.computeIfAbsent(BASE_CURRENCY_CHAR, (c) ->
                            BigDecimal.valueOf(1)
                                    .divide(baseRate.getRate(), baseRate.getRate().scale(), RoundingMode.HALF_UP)
                    );
                }
            }

        } else if (!BASE_CURRENCY_CHAR.equals(base)) {
            throw new ItemNotFoundException("Rates for selected base currency " + base
                    + " and period " + filter.getStartDate()
                    + "::" + filter.getEndDate() + " not found");
        }

        result.setBase(base);
        result.setStartDate(filter.getStartDate());
        result.setEndDate(filter.getEndDate());
        result.setRates(dateMap);

        return result;
    }

    private <T extends BasicFilter> T prepareFilter(T filter) {
        String base = filter.getBase();
        List<String> charCodes = filter.getSymbols();
        if (base == null || "".equals(base)) {
            filter.setBase(BASE_CURRENCY_CHAR);
        } else if (charCodes != null && !charCodes.isEmpty()) {
            filter.getSymbols().add(base);
        }

        return filter;
    }

    @Override
    public void updateRatesData(Envelope envelope) {
        log.info("updating rates started");
        for (DailyData dailyData : envelope.getCube().getData()) {
            if (!exchangeDateRepository.findByDate(dailyData.getTime()).isPresent()) {
                this.saveDailyRates(dailyData);
            }
        }
        log.info("updating rates completed");
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

            rate.setRate(rateData.getRate());
            rate.setCurrency(currency);
            rate.setDate(exDate);
            exDate.getRates().add(rate);

            initRates.add(rate);
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
