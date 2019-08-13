package me.vitblokhin.ecbratesapi.client;

import me.vitblokhin.ecbratesapi.dto.json.DailyRateDto;
import me.vitblokhin.ecbratesapi.dto.json.TimeSeriesRateDto;
import me.vitblokhin.ecbratesapi.client.response.Envelope;

public interface RateClient {

    DailyRateDto fetchDailyRates();

    TimeSeriesRateDto fetchHistoricalRates();

    TimeSeriesRateDto fetchLast90DaysRates();

    Envelope fetchAll();
    Envelope fetchLatest();
} // interface RateClient
