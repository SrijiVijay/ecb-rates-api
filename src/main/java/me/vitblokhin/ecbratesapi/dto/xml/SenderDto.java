package me.vitblokhin.ecbratesapi.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
public class SenderDto {
    @JacksonXmlProperty(localName = "name")
    private String name;

    public SenderDto() {
    }
} // class Sender
