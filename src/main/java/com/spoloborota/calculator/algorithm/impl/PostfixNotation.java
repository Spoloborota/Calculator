package com.spoloborota.calculator.algorithm.impl;

import com.spoloborota.calculator.algorithm.Calculator;
import com.spoloborota.calculator.algorithm.WrongExpressionException;

import java.util.*;
import java.lang.*;

class PostfixNotation implements Calculator {
    private static final List<String> OPERATORS = Arrays.asList("+", "-", "*", "/", "^");
    private static final List<String> DELIMITERS = Arrays.asList("+", "-", "*", "/", "^", "(", ")", " ");
    private static final String DELIMITERS_STRING = "() +-*/^";
    private static boolean flag = true;

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

    private static List<String> parse(String infix) {
        List<String> postfix = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();
        StringTokenizer tokenizer = new StringTokenizer(infix, DELIMITERS_STRING, true);
        String prev = "";
        String curr = "";
        while (tokenizer.hasMoreTokens()) {
            curr = tokenizer.nextToken();
            if (!tokenizer.hasMoreTokens() && isOperator(curr)) {
                System.out.println("Некорректное выражение.");
                flag = false;
                return postfix;
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
                                System.out.println("Wrong brackets");
                                flag = false;
                                return postfix;
                            }
                        }
                        stack.pop();
                        break;
                    default:
                        if (curr.equals("-") && (prev.equals("") || (isDelimiter(prev) && !prev.equals(")")))) {
                            // унарный минус
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
                postfix.add(curr);
            }
            prev = curr;
        }

        while (!stack.isEmpty()) {
            if (isOperator(stack.peek())){
                postfix.add(stack.pop());
            }
            else {
                System.out.println("Wrong brackets");
                flag = false;
                return postfix;
            }
        }
        return postfix;
    }

    private static Double calc(List<String> postfix) {
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
        return stack.pop();
    }
    public static void main (String[] args) {
        while(true) {
            Scanner in = new Scanner(System.in);
            String s = in.nextLine();
            List<String> expression = PostfixNotation.parse(s);
            if (flag) {
                for (String x : expression) System.out.print(x + " ");
                System.out.println();
                System.out.println(calc(expression));
            }
        }
    }


    @Override
    public Double calculate(String expression) throws WrongExpressionException {
        List<String> lst = PostfixNotation.parse(expression);
        if (flag) {
            return calc(lst);
        } else {
            throw new WrongExpressionException("Bad expression");
        }
    }
}
