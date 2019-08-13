package me.vitblokhin.ecbratesapi.client.impl;

import lombok.extern.log4j.Log4j2;
import me.vitblokhin.ecbratesapi.client.RateClient;
import me.vitblokhin.ecbratesapi.client.response.Envelope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Log4j2
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
    public Envelope fetchAll() {
        log.info("fetchAll");
        return this.fetchData(URL_HISTORIC);
    }

    @Override
    public Envelope fetchLatest() {
        log.info("fetchLatest");
        return this.fetchData(URL_DAILY);
    }

    @Override
    public Envelope fetchLast90DaysRates() {
        log.info("fetchLast90DaysRates");
        return this.fetchData(URL_LAST_90_DAYS);
    }

    private Envelope fetchData(String url) {
        url = (url == null) ? URL_DAILY : url;
        return restTemplate.getForEntity(URL_BASE + url, Envelope.class).getBody();
    }

} // class RateClientImpl
