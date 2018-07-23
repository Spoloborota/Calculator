package com.spoloborota.calculator.service;

import com.spoloborota.calculator.entity.Calculation;
import com.spoloborota.calculator.exception.WrongExpressionException;
import com.spoloborota.calculator.exception.WrongOperationException;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

public interface ICalculationServiceAsync {
    CompletableFuture<Double> calculateExpression(String expr) throws WrongExpressionException;
    CompletableFuture<Long> countByDate(LocalDate date);
    CompletableFuture<Long> countContainsOp(String operation) throws WrongOperationException;
    CompletableFuture<Page<Calculation>> listByDate(LocalDate date, int page, int size);
    CompletableFuture<Page<Calculation>> listContainsOp(String operation, int page, int size) throws WrongOperationException;
    CompletableFuture<Double> popularNumber();
}
