package com.spoloborota.calculator.dao;

import com.spoloborota.calculator.entity.Calculation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ICalculationDAO {
    void addCalculation(Calculation calc);
    Long countByDate(LocalDate date);
    Long countContainsOp(String operation);
    Page<Calculation> listByDate(LocalDate date, Pageable page);
    Page<Calculation> listContainsOp(String operation, Pageable page);
    Page<String> getAllExpressions(Pageable page);
}
