package me.vitblokhin.ecbratesapi.client.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import lombok.Data;

@Data
public class Sender {
    @JacksonXmlProperty(localName = "name")
    private String name;

    public Sender() {
    }
} // class Sender
