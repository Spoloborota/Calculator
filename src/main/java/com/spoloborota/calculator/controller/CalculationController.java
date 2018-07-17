package com.spoloborota.calculator.controller;

import com.spoloborota.calculator.exception.WrongOperationException;
import com.spoloborota.calculator.exception.WrongExpressionException;
import com.spoloborota.calculator.entity.Calculation;
import com.spoloborota.calculator.service.ICalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController()
@RequestMapping("/")
public class CalculationController {

    private final ICalculationService calculationService;

    @Autowired
    public CalculationController(ICalculationService calculationService) {
        this.calculationService = calculationService;
    }

    @PostMapping("calculate")
    public ResponseEntity<Double> calculateExpression(@RequestBody String expression) throws WrongExpressionException {
        return new ResponseEntity<>(calculationService.calculateExpression(expression), HttpStatus.OK);
    }

    @PostMapping("count")
    public ResponseEntity<Integer> count(@RequestBody LocalDate date) {
        return new ResponseEntity<>(calculationService.countByDate(date), HttpStatus.OK);
    }

    @PostMapping("operation")
    public ResponseEntity<Integer> operation(@RequestBody String operation) throws WrongOperationException {
        return new ResponseEntity<>(calculationService.countContainsOp(operation), HttpStatus.OK);
    }

    @PostMapping("onDate")
    public ResponseEntity<List<Calculation>> onDate(@RequestBody LocalDate date) {
        return new ResponseEntity<>(calculationService.listByDate(date), HttpStatus.OK);
    }

    @PostMapping("onOperation")
    public ResponseEntity<List<Calculation>> onOperation(@RequestBody String operation) throws WrongOperationException {
        return new ResponseEntity<>(calculationService.listContainsOp(operation), HttpStatus.OK);
    }

    @GetMapping("popular")
    public ResponseEntity<Double> popular() {
        return new ResponseEntity<>(calculationService.popularNumber(), HttpStatus.OK);
    }



}
