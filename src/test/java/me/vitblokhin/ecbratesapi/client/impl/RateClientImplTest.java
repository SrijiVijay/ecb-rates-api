package me.vitblokhin.ecbratesapi.client.impl;

import me.vitblokhin.ecbratesapi.client.response.Envelope;
import me.vitblokhin.ecbratesapi.exception.RateClientException;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import static org.mockito.Mockito.when;

public class RateClientImplTest {
    private final String URI_ALL = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist.xml";
    private final String URI_LATEST = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-daily.xml";
    private final String URI_LAST_90_DAYS = "https://www.ecb.europa.eu/stats/eurofxref/eurofxref-hist-90d.xml";

    @Mock
    private RestTemplate restTemplate;

    private RateClientImpl rateClient;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        rateClient = new RateClientImpl(restTemplate);
    }

    @Test(expected = RateClientException.class)
    public void testFetchAllThrowsRateClientException() {
        String message = "Rest client error";
        when(restTemplate.getForEntity(URI_ALL, Envelope.class)).thenThrow(new RestClientException(message));

        rateClient.fetchAll();
    }

    @Test(expected = RateClientException.class)
    public void testFetchLast90DaysRatesThrowsRateClientException() {
        String message = "Rest client error";
        when(restTemplate.getForEntity(URI_LAST_90_DAYS, Envelope.class)).thenThrow(new RestClientException(message));

        rateClient.fetchLast90DaysRates();
    }

    @Test(expected = RateClientException.class)
    public void testFetchLatestThrowsRateClientException() {
        String message = "Rest client error";
        when(restTemplate.getForEntity(URI_LATEST, Envelope.class)).thenThrow(new RestClientException(message));

        rateClient.fetchLatest();
    }

}