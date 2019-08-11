package me.vitblokhin.ecbratesapi.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RateDto {
    @JacksonXmlProperty(isAttribute = true, localName = "currency")
    private String currency;
    @JacksonXmlProperty(isAttribute = true, localName = "rate")
    private BigDecimal rate;

    public RateDto() {
    }
} // class RateDto
