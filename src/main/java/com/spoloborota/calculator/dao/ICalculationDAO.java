package com.spoloborota.calculator.dao;

import com.spoloborota.calculator.entity.Calculation;

import java.util.List;

public interface ICalculationDAO {
    void addCalculation(Calculation calc);
    List<Calculation> getAllCalculations();
}
