package com.spoloborota.calculator.dao;

import com.spoloborota.calculator.entity.Calculation;

import java.time.LocalDate;
import java.util.List;

public interface ICalculationDAO {
    void addCalculation(Calculation calc);
    Long countByDate(LocalDate date);
    Long countContainsOp(String operation);
    List<Calculation> listByDate(LocalDate date);
    List<Calculation> listContainsOp(String operation);
    Double popularNumber();
}
