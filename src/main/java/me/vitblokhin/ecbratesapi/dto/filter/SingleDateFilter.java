package me.vitblokhin.ecbratesapi.dto.filter;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import me.vitblokhin.ecbratesapi.validation.constraints.ValidNotFutureDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class SingleDateFilter extends BasicFilter {
    @ValidNotFutureDate
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
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
