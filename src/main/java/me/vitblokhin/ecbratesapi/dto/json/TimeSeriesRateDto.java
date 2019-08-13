package me.vitblokhin.ecbratesapi.dto.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class TimeSeriesRateDto {
    private String base;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;
    @JsonProperty("rates")
    private Map<LocalDate, Map<String, BigDecimal>> rates;
} // class HistoricRateDto
