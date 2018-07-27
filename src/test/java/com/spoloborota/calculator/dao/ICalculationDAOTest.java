package com.spoloborota.calculator.dao;

import com.spoloborota.calculator.entity.Calculation;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ICalculationDAOTest {

    @Autowired
    private ICalculationDAO calculationDAO;

    @Test
    public void addCalculation() {
        calculationDAO.addCalculation(new Calculation("2018-07-20", "10^2", 100.0));
        Calculation result = calculationDAO.listByDate("2018-07-20", PageRequest.of(0, 1))
                .getContent().get(0);
        assertEquals("2018-07-20", result.getDate());
        assertEquals("10^2", result.getExpression());
        assertEquals(Double.valueOf(100.0), result.getResult());
    }

    @Test
    public void countByDate() {
        Long result = calculationDAO.countByDate("2018-07-18");
        assertEquals(Long.valueOf(2L), result);
    }

    @Test
    public void countContainsOp() {
        Long result = calculationDAO.countContainsOp("*");
        assertEquals(Long.valueOf(2L), result);
    }

    @Test
    public void listByDate() {
        int result = calculationDAO.listByDate("2018-07-18", PageRequest.of(0, 10))
                .getContent().size();
        assertEquals(2, result);
    }

    @Test
    public void listContainsOp() {
        int result = calculationDAO.listContainsOp("*", PageRequest.of(0, 10))
                .getContent().size();
        assertEquals(2, result);
    }

    @Test
    public void getAllExpressions() {
        int result = calculationDAO.getAllExpressions(PageRequest.of(0, 2))
                .getContent().size();
        assertEquals(2, result);
    }
}