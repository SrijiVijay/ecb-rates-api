package me.vitblokhin.ecbratesapi.dto.json;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@Data
public class RateDto {
    private Map<DailyRateDto, BigDecimal> rates;
} // class RateDto
