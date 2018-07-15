package com.spoloborota.calculator.entity;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Entity
@Table(name="calculation")
public class Calculation implements Serializable {
    private static final long serialVersionUID = 1L;

    public Calculation(LocalDate date, String expression, Double result) {
        this.date = date;
        this.expression = expression;
        this.result = result;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column
    private LocalDate date;

    @Column
    private String expression;

    @Column
    private Double result;
}
