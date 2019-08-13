package me.vitblokhin.ecbratesapi.scheduler;

import lombok.extern.log4j.Log4j2;
import me.vitblokhin.ecbratesapi.client.RateClient;
import me.vitblokhin.ecbratesapi.client.response.Envelope;
import me.vitblokhin.ecbratesapi.service.RateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class RatesFetchScheduler {

    private final RateService rateService;
    private final RateClient rateClient;

    @Autowired
    public RatesFetchScheduler(RateService rateService, RateClient rateClient) {
        this.rateService = rateService;
        this.rateClient = rateClient;
    }

    //@Scheduled(cron = "0 * * * * *")
    @Scheduled(cron = "${cron.fetchtime}")
    public void updateRates() {
        log.info("Running scheduled rates update task");

        Envelope envelope = rateClient.fetchLast90DaysRates();

        rateService.updateRatesData(envelope);

    }
} // class RatesFetchScheduler
