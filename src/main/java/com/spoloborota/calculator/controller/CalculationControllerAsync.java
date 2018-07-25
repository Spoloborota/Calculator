package com.spoloborota.calculator.controller;

import com.spoloborota.calculator.entity.Calculation;
import com.spoloborota.calculator.exception.WrongExpressionException;
import com.spoloborota.calculator.service.ICalculationServiceAsync;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.time.LocalDate;
import java.util.concurrent.CompletableFuture;

@RestController
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
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("count")
    public CompletableFuture<ResponseEntity<BigInteger>> count(@RequestBody LocalDate date) {
        return calculationServiceAsync
                .countByDate(date)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("operation")
    public CompletableFuture<ResponseEntity<BigInteger>> operation(@RequestParam String operation) {
        return calculationServiceAsync
                .countContainsOp(operation)
                .thenApply(ResponseEntity::ok);
    }

    @PostMapping("onDate")
    public CompletableFuture<ResponseEntity<Page<Calculation>>> onDate(@RequestBody LocalDate date, @RequestParam int page, @RequestParam int size) {
        return calculationServiceAsync
                .listByDate(date, page, size)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("onOperation")
    public CompletableFuture<ResponseEntity<Page<Calculation>>> onOperation(@RequestParam String operation, @RequestParam int page, @RequestParam int size) {
        return calculationServiceAsync
                .listContainsOp(operation, page, size)
                .thenApply(ResponseEntity::ok);
    }

    @GetMapping("popular")
    public CompletableFuture<ResponseEntity<Double>> popular() {
        return calculationServiceAsync
                .popularNumber()
                .thenApply(ResponseEntity::ok);
    }

}
