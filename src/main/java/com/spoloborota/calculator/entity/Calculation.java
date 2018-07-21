package com.spoloborota.calculator.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;

@NoArgsConstructor
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
