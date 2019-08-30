package me.vitblokhin.ecbratesapi.config;

import lombok.extern.log4j.Log4j2;
import me.vitblokhin.ecbratesapi.client.RateClient;
import me.vitblokhin.ecbratesapi.client.response.Envelope;
import me.vitblokhin.ecbratesapi.exception.RateClientException;
import me.vitblokhin.ecbratesapi.model.Currency;
import me.vitblokhin.ecbratesapi.repository.CurrencyRepository;
import me.vitblokhin.ecbratesapi.repository.ExchangeDateRepository;
import me.vitblokhin.ecbratesapi.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    @Value("${currency.base}")
    private String BASE_CURRENCY_CHAR;
    private Boolean isSet = false;

    private final CurrencyRepository currencyRepository;
    private final ExchangeDateRepository exchangeDateRepository;
    private final RateService rateService;
    private final RateClient rateClient;

    @Autowired
    public InitialDataLoader(CurrencyRepository currencyRepository,
                             ExchangeDateRepository exchangeDateRepository,
                             RateService rateService,
                             RateClient rateClient) {
        this.currencyRepository = currencyRepository;
        this.exchangeDateRepository = exchangeDateRepository;
        this.rateService = rateService;
        this.rateClient = rateClient;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {

        if (this.isSet) {
            return;
        }

        if (!currencyRepository.findByCharCode(BASE_CURRENCY_CHAR).isPresent()) {
            Currency euro = new Currency();
            euro.setCharCode(BASE_CURRENCY_CHAR);
            euro.setDecription(BASE_CURRENCY_CHAR + " currency");
            currencyRepository.save(euro);
        }

        if (exchangeDateRepository.count() == 0) {
            try {
                Envelope envelope = rateClient.fetchAll();

                rateService.updateRatesData(envelope);
            } catch (RateClientException e) {
                log.error(e);
            }
        }
        this.isSet = true;
    }
} // class InitialDataLoader
