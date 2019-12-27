package me.vitblokhin.ecbratesapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "symbols")
public class Currency extends AbstractEntity {
    @Column(name = "char_code")
    private String charCode;
    @Column(name = "description")
    private String description;
} // class Currency
