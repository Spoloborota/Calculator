package com.spoloborota.calculator.service.impl;

import com.spoloborota.calculator.common.Constants;
import com.spoloborota.calculator.exception.WrongOperationException;
import com.spoloborota.calculator.algorithm.Calculator;
import com.spoloborota.calculator.exception.WrongExpressionException;
import com.spoloborota.calculator.dao.ICalculationDAO;
import com.spoloborota.calculator.entity.Calculation;
import com.spoloborota.calculator.service.ICalculationService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

@Log4j2
@Service
public class CalculationService implements ICalculationService {

    private static final int PAGE_SIZE = 5;
    private static final int FIRST_PAGE = 0;
    private static final Long INIT_VALUE = 1L;
    private static final Double DEFAULT_RESULT = 0.0;
    private static final List<String> OPERATIONS = Arrays.asList("+", "-", "*", "/");

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
    public Page<Calculation> listByDate(LocalDate date, int page, int size) {
        return calculationDAO.listByDate(date, PageRequest.of(page, size));
    }

    @Override
    public Page<Calculation> listContainsOp(String operation, int page, int size) throws WrongOperationException {
        if (OPERATIONS.contains(operation)) {
            return calculationDAO.listContainsOp(operation, PageRequest.of(page, size));
        } else {
            throw new WrongOperationException("Incorrect operation: " + operation);
        }
    }

    @Override
    public Double popularNumber() {
        HashMap<Double, Long> numbersCount = new HashMap<>();
        int currentPage = FIRST_PAGE;
        while(true) {
            Page<String> page = calculationDAO.getAllExpressions(PageRequest.of(currentPage, PAGE_SIZE));
            if(!page.getContent().isEmpty()) {
                parseAndCount(page.getContent(), numbersCount);
                currentPage++;
            } else {
                break;
            }
        }
        if (!numbersCount.isEmpty()) {
            return Collections.max(numbersCount.entrySet(), Comparator.comparingLong(Map.Entry::getValue)).getKey();
        } else {
            return DEFAULT_RESULT;
        }
    }

    private void parseAndCount (List<String> page, Map<Double, Long> numbersCount) {
        for(String exp : page) {
            StringTokenizer tokenizer = new StringTokenizer(exp, Constants.DELIMITERS_STRING, false);
            while (tokenizer.hasMoreTokens()) {
                String number = tokenizer.nextToken();
                if (Constants.INT_OR_FLOAT.matcher(number).matches()) {
                    numbersCount.merge(Double.valueOf(number), INIT_VALUE, (long1, long2) -> long1 + long2);
                } else {
                    log.error("Inconsistent data in db, expression: " + exp);
                }
            }
        }
    }
}
