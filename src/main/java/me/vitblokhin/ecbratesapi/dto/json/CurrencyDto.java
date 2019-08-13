package me.vitblokhin.ecbratesapi.dto.json;

import lombok.Data;
import me.vitblokhin.ecbratesapi.model.Currency;

import java.io.Serializable;

@Data
public class CurrencyDto implements Serializable {
    private String charCode;
    private String description;

    public CurrencyDto() {
    }

    public CurrencyDto(Currency entity) {
        this.charCode = entity.getCharCode();
        this.description = entity.getDecription();
    }
} // class CurrencyDto
