package me.vitblokhin.ecbratesapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "rates")
public class Rate extends AbstractEntity {
    @Column(name = "rate")
    private BigDecimal rate;

    @ManyToOne
    @JoinColumn(name = "currency_id", referencedColumnName = "id")
    private Currency currency;

    @ManyToOne
    @JoinColumn(name = "date_id", referencedColumnName = "id")
    private ExchangeDate date;
} // class Rate
