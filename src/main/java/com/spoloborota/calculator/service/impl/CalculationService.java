package com.spoloborota.calculator.service.impl;

import com.spoloborota.calculator.exception.WrongOperationException;
import com.spoloborota.calculator.algorithm.Calculator;
import com.spoloborota.calculator.exception.WrongExpressionException;
import com.spoloborota.calculator.dao.ICalculationDAO;
import com.spoloborota.calculator.entity.Calculation;
import com.spoloborota.calculator.service.ICalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

@Service
public class CalculationService implements ICalculationService {

    private final static List<String> OPERATIONS = Arrays.asList("+", "-", "*", "/");

    private final ICalculationDAO calculationDAO;

    private final Calculator calculator;

    @Autowired
    public CalculationService(ICalculationDAO calculationDAO, Calculator calculator) {
        this.calculationDAO = calculationDAO;
        this.calculator = calculator;
    }

    @Override
    public Double calculateExpression(String expr) throws WrongExpressionException {
        Double result = calculator.calculate(expr);
        calculationDAO.addCalculation(new Calculation(LocalDate.now(), expr, result));
        return result;
    }

    @Override
    public Long countByDate(LocalDate date) {
        return calculationDAO.countByDate(date);
    }

    @Override
    public Long countContainsOp(String operation) throws WrongOperationException {
        if (OPERATIONS.contains(operation)) {
            return calculationDAO.countContainsOp(operation);
        } else {
            throw new WrongOperationException("Incorrect operation: " + operation);
        }
    }

    @Override
    public List<Calculation> listByDate(LocalDate date) {
        return calculationDAO.listByDate(date);
    }

    @Override
    public List<Calculation> listContainsOp(String operation) throws WrongOperationException {
        if (OPERATIONS.contains(operation)) {
            return calculationDAO.listContainsOp(operation);
        } else {
            throw new WrongOperationException("Incorrect operation: " + operation);
        }
    }

    @Override
    public Double popularNumber() {
        return null;
    }
}
