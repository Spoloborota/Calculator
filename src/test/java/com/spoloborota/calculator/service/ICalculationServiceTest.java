package com.spoloborota.calculator.service;

import com.spoloborota.calculator.algorithm.impl.PostfixNotation;
import com.spoloborota.calculator.dao.ICalculationDAO;
import com.spoloborota.calculator.dao.impl.CalculationDAO;
import com.spoloborota.calculator.exception.WrongExpressionException;
import com.spoloborota.calculator.exception.WrongOperationException;
import com.spoloborota.calculator.service.impl.CalculationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(SpringRunner.class)
public class ICalculationServiceTest {

    ICalculationService calculationService;
    ICalculationDAO calculationDAO = mock(CalculationDAO.class);

    @Before
    public void init() {
        List<String> lst = Arrays.asList("(-7*8+9-(9/4.5))^2", "9*1+4.5");
        PageImpl<String> pageFirst = new PageImpl<>(lst, PageRequest.of(0, 3), 2);
        PageImpl<String> pageSecond = new PageImpl<>(new ArrayList<String>(), PageRequest.of(0, 3), 0);
        when(calculationDAO.getAllExpressions(any())).thenReturn(pageFirst).thenReturn(pageSecond);
        calculationService = new CalculationService(calculationDAO, new PostfixNotation());
    }

    @Test
    public void calculateExpression() throws WrongExpressionException {
        Double aDouble = calculationService.calculateExpression("(-7*8+9-(9/4.5))^2");
        assertEquals(Double.valueOf(2401.0), aDouble);
    }

    @Test(expected = WrongOperationException.class)
    public void countContainsOp() throws WrongOperationException {
        calculationService.countContainsOp("XXX");
    }

    @Test(expected = WrongOperationException.class)
    public void listContainsOp() throws WrongOperationException {
        calculationService.listContainsOp("YYY", 0, 1);
    }

    @Test
    public void popularNumber() {
        assertEquals(Double.valueOf(9.0), calculationService.popularNumber());
    }
}