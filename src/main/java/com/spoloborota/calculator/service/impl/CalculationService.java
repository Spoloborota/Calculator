package com.spoloborota.calculator.service.impl;

import com.spoloborota.calculator.algorithm.Calculator;
import com.spoloborota.calculator.algorithm.WrongExpressionException;
import com.spoloborota.calculator.dao.ICalculationDAO;
import com.spoloborota.calculator.entity.Calculation;
import com.spoloborota.calculator.service.ICalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class CalculationService implements ICalculationService {

    @Autowired
    private ICalculationDAO calculationDAO;

    @Autowired
    private Calculator calculator;

    @Override
    public List<Calculation> getAllCalculations() {
        return calculationDAO.getAllCalculations();
    }

    @Override
    public Double calculateExpression(String expr) {
        try {
            Double result = calculator.calculate(expr);
            calculationDAO.addCalculation(new Calculation(LocalDate.now(), expr, result));
            return result;
        } catch (WrongExpressionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
