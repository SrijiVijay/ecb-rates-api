package me.vitblokhin.ecbratesapi.service;

import me.vitblokhin.ecbratesapi.client.response.Envelope;
import me.vitblokhin.ecbratesapi.dto.filter.HistoricalFilter;
import me.vitblokhin.ecbratesapi.dto.filter.SingleDateFilter;
import me.vitblokhin.ecbratesapi.dto.json.DailyRateDto;
import me.vitblokhin.ecbratesapi.dto.json.HistoricalRateDto;

public interface RateService {

    DailyRateDto getDailyRate(SingleDateFilter filter);
    HistoricalRateDto getHistoricalRate(HistoricalFilter filter);

    void updateRatesData(Envelope envelope);
} // interface RateService
