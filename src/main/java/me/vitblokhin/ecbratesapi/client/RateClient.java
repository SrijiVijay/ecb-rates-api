package me.vitblokhin.ecbratesapi.client;

import me.vitblokhin.ecbratesapi.client.response.Envelope;
import me.vitblokhin.ecbratesapi.exception.RateClientException;

public interface RateClient {
    Envelope fetchAll() throws RateClientException;
    Envelope fetchLatest() throws RateClientException;
    Envelope fetchLast90DaysRates() throws RateClientException;
} // interface RateClient
