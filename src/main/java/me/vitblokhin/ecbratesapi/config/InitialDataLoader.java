package me.vitblokhin.ecbratesapi.config;

import lombok.extern.log4j.Log4j2;
import me.vitblokhin.ecbratesapi.client.RateClient;
import me.vitblokhin.ecbratesapi.client.response.DailyData;
import me.vitblokhin.ecbratesapi.client.response.Envelope;
import me.vitblokhin.ecbratesapi.client.response.RateData;
import me.vitblokhin.ecbratesapi.model.Currency;
import me.vitblokhin.ecbratesapi.model.ExchangeDate;
import me.vitblokhin.ecbratesapi.model.Rate;
import me.vitblokhin.ecbratesapi.repository.CurrencyRepository;
import me.vitblokhin.ecbratesapi.repository.ExchangeDateRepository;
import me.vitblokhin.ecbratesapi.repository.RateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Log4j2
@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final String EURO_CHAR_CODE = "EUR";
    private Boolean isSet = false;

    private final CurrencyRepository currencyRepository;
    private final ExchangeDateRepository exchangeDateRepository;
    private final RateRepository rateRepository;
    private final RateClient rateClient;

    @Autowired
    public InitialDataLoader(CurrencyRepository currencyRepository,
                             ExchangeDateRepository exchangeDateRepository,
                             RateRepository rateRepository,
                             RateClient rateClient) {
        this.currencyRepository = currencyRepository;
        this.exchangeDateRepository = exchangeDateRepository;
        this.rateRepository = rateRepository;
        this.rateClient = rateClient;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (this.isSet) {
            return;
        }

        if (!currencyRepository.findByCharCode(EURO_CHAR_CODE).isPresent()) {
            Currency euro = new Currency();
            euro.setCharCode(EURO_CHAR_CODE);
            euro.setDecription("EU currency");
            currencyRepository.save(euro);
        }

        if (exchangeDateRepository.count() == 0) {
            Envelope envelope = rateClient.fetchAll();

            for (DailyData dailyData : envelope.getCube().getData()) {
                this.saveDailyRates(dailyData);
            }
        }
        this.isSet = true;
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
        log.info("init: saving rates for date: {}", date);
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
} // class InitialDataLoader
