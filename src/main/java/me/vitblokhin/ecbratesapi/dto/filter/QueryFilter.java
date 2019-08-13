package me.vitblokhin.ecbratesapi.dto.filter;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class QueryFilter {
    private String base;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> symbols;
} // class QueryFilter
