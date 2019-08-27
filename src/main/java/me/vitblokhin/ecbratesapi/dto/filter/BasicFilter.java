package me.vitblokhin.ecbratesapi.dto.filter;

import lombok.Data;
import me.vitblokhin.ecbratesapi.validation.constraints.ValidCurrencyCharCode;
import org.springframework.beans.factory.annotation.Value;

import java.io.Serializable;
import java.util.List;

@Data
public class BasicFilter implements Serializable {
    @Value("${currency.base}")
    @ValidCurrencyCharCode
    protected String base;
    protected List<String> symbols;
} // class BasicFilter
