package com.spoloborota.calculator.service;

import com.spoloborota.calculator.exception.WrongOperationException;
import com.spoloborota.calculator.exception.WrongExpressionException;
import com.spoloborota.calculator.entity.Calculation;

import java.time.LocalDate;
import java.util.List;

public interface ICalculationService {
    Double calculateExpression(String expr) throws WrongExpressionException;
    Long countByDate(LocalDate date);
    Long countContainsOp(String operation) throws WrongOperationException;
    List<Calculation> listByDate(LocalDate date);
    List<Calculation> listContainsOp(String operation) throws WrongOperationException;
    Double popularNumber();
}
