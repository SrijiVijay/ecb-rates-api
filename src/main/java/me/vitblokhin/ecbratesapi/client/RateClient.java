package me.vitblokhin.ecbratesapi.client;

import me.vitblokhin.ecbratesapi.dto.json.DailyRateDto;
import me.vitblokhin.ecbratesapi.dto.json.TimeSeriesRateDto;
import me.vitblokhin.ecbratesapi.dto.xml.EnvelopeDto;

public interface RateClient {

    DailyRateDto fetchDailyRates();

    TimeSeriesRateDto fetchHistoricalRates();

    TimeSeriesRateDto fetchLast90DaysRates();

    EnvelopeDto fetchAll();
} // interface RateClient
