package me.vitblokhin.ecbratesapi.client.response;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;


@Data
@JacksonXmlRootElement(localName = "Envelope")
public class Envelope {
    private String subject;
    @JacksonXmlProperty(localName = "Sender")
    private Sender sender;
    @JacksonXmlProperty(localName = "Cube")
    private Cube cube;

    public Envelope() {
    }
} // class Envelope
