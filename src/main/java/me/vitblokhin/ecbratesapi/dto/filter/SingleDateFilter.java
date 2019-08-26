package me.vitblokhin.ecbratesapi.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SingleDateFilter extends BasicFilter {
    private LocalDate date;

    public SingleDateFilter(BasicFilter filter, LocalDate date) {
        this.base = filter.getBase();
        this.symbols = filter.getSymbols();
        this.date = date;
    }

    public SingleDateFilter() {
        super();
    }
} // class SingleDateFilter
