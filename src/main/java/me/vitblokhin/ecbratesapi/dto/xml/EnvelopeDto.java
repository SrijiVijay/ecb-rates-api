package me.vitblokhin.ecbratesapi.dto.xml;

import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement;
import lombok.Data;


@Data
@JacksonXmlRootElement(localName = "Envelope")
public class EnvelopeDto {
    private String subject;
    @JacksonXmlProperty(localName = "Sender")
    private SenderDto sender;
    @JacksonXmlProperty(localName = "Cube")
    private CubeDto cube;

    public EnvelopeDto() {
    }
} // class EnvelopeDto
