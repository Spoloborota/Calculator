package com.spoloborota.calculator.algorithm.impl;

import com.spoloborota.calculator.algorithm.Calculator;
import com.spoloborota.calculator.algorithm.WrongExpressionException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

import java.util.*;
import java.lang.*;
import java.util.regex.Pattern;

@Log4j2
@Component
class PostfixNotation implements Calculator {
    private static final List<String> OPERATORS = Arrays.asList("+", "-", "*", "/", "^");
    private static final List<String> DELIMITERS = Arrays.asList("+", "-", "*", "/", "^", "(", ")", " ");
    private static final String DELIMITERS_STRING = "() +-*/^";
    private static final Pattern INT_OR_FLOAT = Pattern.compile("^[0-9]*[.]?[0-9]+$");

    private static boolean isDelimiter(String token) {
        if (token.length() != 1) {
            return false;
        }
        return DELIMITERS.contains(token);
    }

    private static boolean isOperator(String token) {
        return token.equals("u-") || OPERATORS.contains(token);
    }

    private static int priorityOf(String token) {
        switch (token) {
            case "(":
                return 1;
            case "+":
            case "-":
                return 2;
            case "*":
            case "/":
                return 3;
            case "^":
                return 4;
            default:
                return 4;
        }
    }

    private static void logAndThrow(String message) throws WrongExpressionException {
        log.error(message);
        throw new WrongExpressionException(message);
    }

    private static List<String> parse(String infix) throws WrongExpressionException {
        log.info(() -> "Begin to process expression: " + infix);
        List<String> postfix = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(infix, DELIMITERS_STRING, true);
        String prev = "";
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens() && isOperator(curr)) {
                logAndThrow("Bad expression. Current expression stack: " + postfix);
            }
            if (curr.equals(" ")) {
                continue;
            }
            else if (isDelimiter(curr)) {
                switch (curr) {
                    case "(":
                        stack.push(curr);
                        break;
                    case ")":
                        while (!Objects.equals(stack.peek(), "(")) {
                            postfix.add(stack.pop());
                            if (stack.isEmpty()) {
                                logAndThrow("Wrong brackets. Current expression stack: " + postfix);
                            }
                        }
                        stack.pop();
                        break;
                    default:
                        if (curr.equals("-") && (prev.equals("") || (isDelimiter(prev) && !prev.equals(")")))) {
                            curr = "u-";
                        } else {
                            while (!stack.isEmpty() && (priorityOf(curr) <= priorityOf(stack.peek()))) {
                                postfix.add(stack.pop());
                            }
                        }
                        stack.push(curr);
                        break;
                }
            } else {
                if (INT_OR_FLOAT.matcher(curr).matches()) {
                    postfix.add(curr);
                } else {
                    logAndThrow("Unrecognized symbols: " + curr);
                }
            }
            prev = curr;
            log.debug(() -> "Temp postfix notation: " + postfix);
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())){
                postfix.add(stack.pop());
            }
            else {
                logAndThrow("Wrong brackets. Current expression stack: " + postfix);
            }
        }
        return postfix;
    }

    private static Double calc(List<String> postfix) {
        log.info(() -> "Begin to calculate postfix notation expression: " + postfix);
        Deque<Double> stack = new ArrayDeque<>();
        for (String x : postfix) {
            switch (x) {
                case "+":
                    stack.push(stack.pop() + stack.pop());
                    break;
                case "-": {
                    Double b = stack.pop(), a = stack.pop();
                    stack.push(a - b);
                    break;
                }
                case "*":
                    stack.push(stack.pop() * stack.pop());
                    break;
                case "/": {
                    Double b = stack.pop(), a = stack.pop();
                    stack.push(a / b);
                    break;
                }
                case "^": {
                    Double b = stack.pop(), a = stack.pop();
                    stack.push(Math.pow(a, b));
                    break;
                }
                case "u-":
                    stack.push(-stack.pop());
                    break;
                default:
                    stack.push(Double.valueOf(x));
                    break;
            }
        }
        log.info(() -> "Calculation result: " + stack.peek());
        return stack.pop();
    }

    @Override
    public Double calculate(String expression) throws WrongExpressionException {
        return calc(PostfixNotation.parse(expression));
    }

}
