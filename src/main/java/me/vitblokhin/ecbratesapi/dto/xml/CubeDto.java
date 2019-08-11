package me.vitblokhin.ecbratesapi.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class CubeDto {
    @JacksonXmlProperty(localName = "Cube")
    private DataDto data;

    public CubeDto() {
    }
} // class CubeDto
