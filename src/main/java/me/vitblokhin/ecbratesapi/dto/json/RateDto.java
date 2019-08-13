package me.vitblokhin.ecbratesapi.dto.json;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Data
public class RateDto implements Serializable {
    private String currency;
    private BigDecimal rate;
    private LocalDate date;
} // class RateDto
