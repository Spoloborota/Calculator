package com.spoloborota.calculator.service;

import com.spoloborota.calculator.exception.WrongOperationException;
import com.spoloborota.calculator.exception.WrongExpressionException;
import com.spoloborota.calculator.entity.Calculation;
import org.springframework.data.domain.Page;

import java.time.LocalDate;

public interface ICalculationService {
    Double calculateExpression(String expr) throws WrongExpressionException;
    Long countByDate(LocalDate date);
    Long countContainsOp(String operation) throws WrongOperationException;
    Page<Calculation> listByDate(LocalDate date, int page, int size);
    Page<Calculation> listContainsOp(String operation, int page, int size) throws WrongOperationException;
    Double popularNumber();
}
