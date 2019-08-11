package me.vitblokhin.ecbratesapi.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;

@Data
@JacksonXmlRootElement(localName = "Sender", namespace = "gesmes")
public class SenderDto {
    @JacksonXmlProperty(localName = "name")
    private String name;

    public SenderDto() {
    }
} // class Sender
