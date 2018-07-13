package com.spoloborota.calculator.algorithm;

public interface Calculator {
    Double calculate(String expression) throws WrongExpressionException;
}
