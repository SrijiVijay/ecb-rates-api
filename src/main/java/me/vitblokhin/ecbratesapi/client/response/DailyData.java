package me.vitblokhin.ecbratesapi.client.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DailyData {
    @JsonFormat(pattern = "yyyy-MM-dd")
    @JacksonXmlProperty(isAttribute = true)
    private LocalDate time;
    @JacksonXmlProperty(localName = "Cube")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<RateData> rates;

    public DailyData() {
    }
} // class DailyData
