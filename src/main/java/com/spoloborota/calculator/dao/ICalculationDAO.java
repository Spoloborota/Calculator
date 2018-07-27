package com.spoloborota.calculator.dao;

import com.spoloborota.calculator.entity.Calculation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICalculationDAO {
    void addCalculation(Calculation calc);
    Long countByDate(String date);
    Long countContainsOp(String operation);
    Page<Calculation> listByDate(String date, Pageable page);
    Page<Calculation> listContainsOp(String operation, Pageable page);
    Page<String> getAllExpressions(Pageable page);
}
