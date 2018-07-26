package com.spoloborota.calculator.service.impl;

import com.spoloborota.calculator.algorithm.Calculator;
import com.spoloborota.calculator.common.Constants;
import com.spoloborota.calculator.dao.ICalculationDAOAsync;
import com.spoloborota.calculator.entity.Calculation;
import com.spoloborota.calculator.exception.WrongOperationException;
import com.spoloborota.calculator.service.ICalculationServiceAsync;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
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
    public CompletableFuture<Double> calculateExpression(String expr) {
        CompletableFuture<Double> resultFuture = CompletableFuture.supplyAsync(() -> calculator.calculate(expr));
        resultFuture.thenApplyAsync((d) ->
                calculationDAOAsync.save(new Calculation(LocalDate.now(), expr, d)), fixedThreadPool);
        return resultFuture;
    }

    @Override
    public CompletableFuture<BigInteger> countByDate(String date) {
        return calculationDAOAsync.countByDate(date);
    }

    @Override
    public CompletableFuture<BigInteger> countContainsOp(String operation) {
        if (OPERATIONS.contains(operation)) {
            return calculationDAOAsync.countContainsOp(operation);
        } else {
            throw new WrongOperationException("Incorrect operation: " + operation);
        }
    }

    @Override
    public CompletableFuture<Page<Calculation>> listByDate(String date, int page, int size) {
        return calculationDAOAsync.listByDate(date, PageRequest.of(page, size));
    }

    @Override
    public CompletableFuture<Page<Calculation>> listContainsOp(String operation, int page, int size) {
        if (OPERATIONS.contains(operation)) {
            return calculationDAOAsync.listContainsOp(operation, PageRequest.of(page, size));
        } else {
            throw new WrongOperationException("Incorrect operation: " + operation);
        }
    }

    @Override
    public CompletableFuture<Double> popularNumber() {
        HashMap<Double, Long> numbersCount = new HashMap<>();
        int currentPage = FIRST_PAGE;
        while(true) {
            Page<Calculation> page = calculationDAOAsync.findAll(PageRequest.of(currentPage, PAGE_SIZE));
            if(!page.getContent().isEmpty()) {
                parseAndCount(page.getContent(), numbersCount);
                currentPage++;
            } else {
                break;
            }
        }
        if (!numbersCount.isEmpty()) {
            return CompletableFuture.supplyAsync(() ->
                    Collections.max(numbersCount.entrySet(), Comparator.comparingLong(Map.Entry::getValue)).getKey());
        } else {
            return CompletableFuture.completedFuture(DEFAULT_RESULT);
        }
    }

    private void parseAndCount (List<Calculation> page, Map<Double, Long> numbersCount) {
        for(Calculation calc : page) {
            StringTokenizer tokenizer = new StringTokenizer(calc.getExpression(), Constants.DELIMITERS_STRING, false);
            while (tokenizer.hasMoreTokens()) {
                String number = tokenizer.nextToken();
                if (Constants.INT_OR_FLOAT.matcher(number).matches()) {
                    numbersCount.merge(Double.valueOf(number), INIT_VALUE, (long1, long2) -> long1 + long2);
                } else {
                    log.error("Inconsistent data in db, calculation: " + calc);
                }
            }
        }
    }
}
