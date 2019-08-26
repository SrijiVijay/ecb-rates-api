package me.vitblokhin.ecbratesapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "dates")
public class ExchangeDate extends AbstractEntity {
    @Column(name = "date")
    private LocalDate date;

    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "date", fetch = FetchType.LAZY)
    private List<Rate> rates;
} // class ExchangeDate
