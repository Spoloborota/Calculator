package com.spoloborota.calculator.algorithm;

import com.spoloborota.calculator.exception.WrongExpressionException;

public interface Calculator {
    Double calculate(String expression) throws WrongExpressionException;
}
