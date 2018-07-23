package com.spoloborota.calculator.service.impl;

import com.spoloborota.calculator.algorithm.Calculator;
import com.spoloborota.calculator.dao.ICalculationDAOAsync;
import com.spoloborota.calculator.entity.Calculation;
import com.spoloborota.calculator.exception.WrongExpressionException;
import com.spoloborota.calculator.exception.WrongOperationException;
import com.spoloborota.calculator.service.ICalculationServiceAsync;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Log4j2
@Service
public class CalculationServiceAsync implements ICalculationServiceAsync {

    private static final int PAGE_SIZE = 5;
    private static final int FIRST_PAGE = 0;
    private static final Long INIT_VALUE = 1L;
    private static final Double DEFAULT_RESULT = 0.0;
    private static final List<String> OPERATIONS = Arrays.asList("+", "-", "*", "/");

    private final ICalculationDAOAsync calculationDAOAsync;
    private final Calculator calculator;
    private final Executor fixedThreadPool;

    @Autowired
    public CalculationServiceAsync(ICalculationDAOAsync calculationDAOAsync, Calculator calculator, Executor fixedThreadPool) {
        this.calculationDAOAsync = calculationDAOAsync;
        this.calculator = calculator;
        this.fixedThreadPool = fixedThreadPool;
    }

    @Override
    public CompletableFuture<Double> calculateExpression(String expr) throws WrongExpressionException {
        CompletableFuture<Double> resultFuture = CompletableFuture.supplyAsync(() -> calculator.calculate(expr));
        resultFuture.thenApplyAsync((d) -> (Calculation)calculationDAOAsync.save(new Calculation(LocalDate.now(), expr, d)), fixedThreadPool);
        return resultFuture;
    }

    @Override
    public CompletableFuture<Long> countByDate(LocalDate date) {
        return null;
    }

    @Override
    public CompletableFuture<Long> countContainsOp(String operation) throws WrongOperationException {
        return null;
    }

    @Override
    public CompletableFuture<Page<Calculation>> listByDate(LocalDate date, int page, int size) {
        return null;
    }

    @Override
    public CompletableFuture<Page<Calculation>> listContainsOp(String operation, int page, int size) throws WrongOperationException {
        return null;
    }

    @Override
    public CompletableFuture<Double> popularNumber() {
        return null;
    }
}
