package me.vitblokhin.ecbratesapi.service;

import me.vitblokhin.ecbratesapi.client.response.Envelope;
import me.vitblokhin.ecbratesapi.dto.filter.QueryFilter;
import me.vitblokhin.ecbratesapi.dto.json.DailyRateDto;

import java.time.LocalDate;

public interface RateService {

    DailyRateDto getDailyRate(QueryFilter filter, LocalDate date);

    void updateRatesData(Envelope envelope);
} // interface RateService
