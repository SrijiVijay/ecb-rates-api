package me.vitblokhin.ecbratesapi.config;

import lombok.extern.log4j.Log4j2;
import me.vitblokhin.ecbratesapi.client.RateClient;
import me.vitblokhin.ecbratesapi.client.response.Envelope;
import me.vitblokhin.ecbratesapi.model.Currency;
import me.vitblokhin.ecbratesapi.repository.CurrencyRepository;
import me.vitblokhin.ecbratesapi.repository.ExchangeDateRepository;
import me.vitblokhin.ecbratesapi.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class InitialDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private final String EURO_CHAR_CODE = "EUR";
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

        if (!currencyRepository.findByCharCode(EURO_CHAR_CODE).isPresent()) {
            Currency euro = new Currency();
            euro.setCharCode(EURO_CHAR_CODE);
            euro.setDecription("EU currency");
            currencyRepository.save(euro);
        }

        if (exchangeDateRepository.count() == 0) {
            Envelope envelope = rateClient.fetchAll();

            rateService.updateRatesData(envelope);
        }
        this.isSet = true;
    }
} // class InitialDataLoader
