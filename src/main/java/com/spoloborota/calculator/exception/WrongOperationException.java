package com.spoloborota.calculator.exception;

public class WrongOperationException extends RuntimeException{
    public WrongOperationException(String message) {
        super(message);
    }
}
