package com.spoloborota.calculator.service;

import com.spoloborota.calculator.entity.Calculation;

import java.util.List;

public interface ICalculationService {
    List<Calculation> getAllCalculations();
    Double calculateExpression(String expr);
}
