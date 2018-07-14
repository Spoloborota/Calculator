package com.spoloborota.calculator.algorithm.impl;

import com.spoloborota.calculator.algorithm.WrongExpressionException;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class PostfixNotationTest {

    private final String integers = "3 + 4 * 2 / (1 - 5) + 2";
    private final String fractional = "9333253.00114*0.2221+4.5/3*7800.0009";
    private final String badExpressionFormat1 = "2 % 3 - 7";
    private final String badExpressionFormat2 = "+ 8";
    private final String badExpressionFormat3 = "_";
    private final String fromTask1 = "(-7*8+9-(9/4.5))^2";
    private final String fromTask2 = "9*1+4.5";

    @Test
    public void calculate_integers() throws WrongExpressionException {
        assertEquals(Double.valueOf(3), new PostfixNotation().calculate(integers));
    }

    @Test
    public void calculate_fractional() throws WrongExpressionException {
        assertEquals(Double.valueOf(2084615.492903194), new PostfixNotation().calculate(fractional));
    }

    @Test(expected = WrongExpressionException.class)
    public void calculate_badExpressionFormat1() throws WrongExpressionException {
        new PostfixNotation().calculate(badExpressionFormat1);
    }

    @Test(expected = NoSuchElementException.class)
    public void calculate_badExpressionFormat2() throws WrongExpressionException {
        new PostfixNotation().calculate(badExpressionFormat2);
    }

    @Test(expected = WrongExpressionException.class)
    public void calculate_badExpressionFormat3() throws WrongExpressionException {
        new PostfixNotation().calculate(badExpressionFormat3);
    }

    @Test
    public void calculate_fromTask1() throws WrongExpressionException {
        assertEquals(Double.valueOf(2401), new PostfixNotation().calculate(fromTask1));
    }

    @Test
    public void calculate_fromTask2() throws WrongExpressionException {
        assertEquals(Double.valueOf(13.5), new PostfixNotation().calculate(fromTask2));
    }
}