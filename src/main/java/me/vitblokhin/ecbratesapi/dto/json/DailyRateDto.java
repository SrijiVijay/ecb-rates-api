package me.vitblokhin.ecbratesapi.dto.json;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

@Data
public class DailyRateDto implements Serializable {
    private String base;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
    @JsonProperty("rates")
    private Map<String, BigDecimal> rates;
} // class DailyRateDto
