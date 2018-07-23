package com.spoloborota.calculator.controller;

import com.spoloborota.calculator.exception.WrongExpressionException;
import com.spoloborota.calculator.service.ICalculationServiceAsync;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@Log4j2
@RestController()
@RequestMapping("/async")
public class CalculationControllerAsync {

    private final ICalculationServiceAsync calculationServiceAsync;

    @Autowired
    public CalculationControllerAsync(ICalculationServiceAsync calculationServiceAsync) {
        this.calculationServiceAsync = calculationServiceAsync;
    }

    @PostMapping("calculate")
    public CompletableFuture<ResponseEntity<Double>> calculateExpression(@RequestBody String expression) throws WrongExpressionException {
        return calculationServiceAsync
                .calculateExpression(expression)
                .thenApply(ResponseEntity::ok)
                .exceptionally(handleGetUsersFailure);
    }

    private static Function<Throwable, ResponseEntity<Double>> handleGetUsersFailure = throwable -> {
        log.error("Unable to retrieve users", throwable);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    };
}
