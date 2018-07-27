package com.spoloborota.calculator.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;
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
@EqualsAndHashCode(exclude = {"id","date"})
public class Calculation implements Serializable {
    private static final long serialVersionUID = 1L;

    public Calculation(String date, String expression, Double result) {
        this.date = date;
        this.expression = expression;
        this.result = result;
    }

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id", updatable = false, nullable = false)
    private Long id;

    @Column
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String date;

    @Column
    private String expression;

    @Column
    private Double result;
}
