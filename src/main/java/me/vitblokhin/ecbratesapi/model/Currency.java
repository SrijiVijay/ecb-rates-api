package me.vitblokhin.ecbratesapi.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "symbols")
public class Currency extends AbstractEntity {
    @Column(name = "char_code")
    private String charCode;
    @Column(name = "description")
    private String decription;
} // class Currency
