package me.vitblokhin.ecbratesapi.client;

import me.vitblokhin.ecbratesapi.dto.xml.EnvelopeDto;

public interface RateClient {

    EnvelopeDto fetchDailyRates();

    EnvelopeDto fetchHistoricalRates();

    EnvelopeDto fetchLast90DaysRates();
} // interface RateClient
