package me.vitblokhin.ecbratesapi.client.impl;

import me.vitblokhin.ecbratesapi.client.RateClient;
import me.vitblokhin.ecbratesapi.dto.xml.EnvelopeDto;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component("rateClient")
public class RateClientImpl implements RateClient {
    @Value("${ecb.url}")
    private String URL_BASE;

    private final String URL_DAILY = "/stats/eurofxref/eurofxref-daily.xml";
    private final String URL_HISTORIC = "/stats/eurofxref/eurofxref-hist.xml";
    private final String URL_LAST_90_DAYS = "/stats/eurofxref/eurofxref-hist-90d.xml";

    private final RestTemplate restTemplate;

    public RateClientImpl(RestTemplateBuilder builder) {
        this.restTemplate = builder.build();
    }

    @Override
    public EnvelopeDto fetchDailyRates() {
        return this.fetchData(URL_DAILY);
    }

    @Override
    public EnvelopeDto fetchHistoricalRates() {
        return this.fetchData(URL_HISTORIC);
    }

    @Override
    public EnvelopeDto fetchLast90DaysRates() {
        return this.fetchData(URL_LAST_90_DAYS);
    }

    private EnvelopeDto fetchData(String url){
        return restTemplate.getForEntity(URL_BASE + url, EnvelopeDto.class ).getBody();
    }

} // class RateClientImpl
