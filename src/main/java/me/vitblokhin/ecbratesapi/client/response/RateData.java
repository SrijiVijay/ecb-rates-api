package me.vitblokhin.ecbratesapi.client.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class RateData {
    @JacksonXmlProperty(isAttribute = true, localName = "currency")
    private String currency;
    @JacksonXmlProperty(isAttribute = true, localName = "rate")
    private BigDecimal rate;

    public RateData() {
    }
} // class RateData
