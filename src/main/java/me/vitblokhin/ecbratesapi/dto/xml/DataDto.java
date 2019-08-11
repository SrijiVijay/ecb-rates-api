package me.vitblokhin.ecbratesapi.dto.xml;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class DataDto {

    @JsonFormat(pattern = "yyyy-MM-dd")
    @JacksonXmlProperty(isAttribute = true)
    private LocalDate time;
    @JacksonXmlProperty(localName = "Cube")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<RateDto> rates;

    public DataDto() {
    }
} // class Data
