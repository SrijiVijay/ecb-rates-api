package me.vitblokhin.ecbratesapi.dto.filter;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BasicFilter implements Serializable {
    protected String base;
    protected List<String> symbols;
} // class BasicFilter
