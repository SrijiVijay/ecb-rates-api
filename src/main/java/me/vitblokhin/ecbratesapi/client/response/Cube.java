package me.vitblokhin.ecbratesapi.client.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

import java.util.List;

@Data
public class Cube {
    @JacksonXmlProperty(localName = "Cube")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<DailyData> data;

    public Cube() {
    }
} // class Cube
