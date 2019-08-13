package me.vitblokhin.ecbratesapi.client;

import me.vitblokhin.ecbratesapi.client.response.Envelope;

public interface RateClient {
    Envelope fetchAll();
    Envelope fetchLatest();
    Envelope fetchLast90DaysRates();
} // interface RateClient
