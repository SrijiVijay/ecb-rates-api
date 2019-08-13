package me.vitblokhin.ecbratesapi.client.impl;

import lombok.extern.log4j.Log4j2;
import me.vitblokhin.ecbratesapi.client.RateClient;
import me.vitblokhin.ecbratesapi.dto.json.DailyRateDto;
import me.vitblokhin.ecbratesapi.dto.json.TimeSeriesRateDto;
import me.vitblokhin.ecbratesapi.client.response.DailyData;
import me.vitblokhin.ecbratesapi.client.response.Envelope;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

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
    public DailyRateDto fetchDailyRates() {

        Envelope data = this.fetchData(URL_DAILY);

        DailyRateDto dailyRate = new DailyRateDto();
        dailyRate.setBase("EUR");

        Map<String, BigDecimal> rates = new HashMap<>();
        if (data.getCube().getData() == null || data.getCube().getData().size() != 1) {
            return null;
        }

        dailyRate.setDate(data.getCube().getData().get(0).getTime());
        data.getCube().getData().get(0).getRates().forEach(r -> rates.put(r.getCurrency(), r.getRate()));

        dailyRate.setRates(rates);

        return dailyRate;
    }

    @Override
    public TimeSeriesRateDto fetchHistoricalRates() {
        Envelope data = this.fetchData(URL_HISTORIC);

        TimeSeriesRateDto timeSeriesRate = new TimeSeriesRateDto();
        timeSeriesRate.setBase("EUR");

        Map<LocalDate, Map<String, BigDecimal>> rates = new HashMap<>();
        if (data.getCube().getData() == null || data.getCube().getData().size() == 0) {
            return null;
        }

        for (DailyData dailyData : data.getCube().getData()) {
            Map<String, BigDecimal> dateMap = new HashMap<>();
            dailyData.getRates()
                    .forEach(r -> dateMap.put(r.getCurrency(), r.getRate()));
            rates.put(dailyData.getTime(), dateMap);
        }

        timeSeriesRate.setRates(rates);

        return timeSeriesRate;
    }

    @Override
    public TimeSeriesRateDto fetchLast90DaysRates() {
        return null;
    }

    @Override
    public Envelope fetchAll() {
        return this.fetchData(URL_HISTORIC);
    }

    @Override
    public Envelope fetchLatest() {
        return this.fetchData(URL_DAILY);
    }

    private Envelope fetchData(String url) {
        url = (url == null) ? URL_DAILY : url;
        return restTemplate.getForEntity(URL_BASE + url, Envelope.class).getBody();
    }

} // class RateClientImpl
