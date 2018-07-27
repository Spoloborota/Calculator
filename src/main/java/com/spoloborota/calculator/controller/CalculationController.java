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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
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

    @GetMapping("count")
    public ResponseEntity<Long> count(@RequestParam String date) {
        return new ResponseEntity<>(calculationService.countByDate(date), HttpStatus.OK);
    }

    @GetMapping("operation")
    public ResponseEntity<Long> operation(@RequestParam String operation) throws WrongOperationException {
        return new ResponseEntity<>(calculationService.countContainsOp(operation), HttpStatus.OK);
    }

    @GetMapping("onDate")
    public List<Calculation> onDate(@RequestParam String date, @RequestParam int page, @RequestParam int size) {
        return calculationService.listByDate(date, page, size).getContent();
    }

    @GetMapping("onOperation")
    public List<Calculation> onOperation(@RequestParam String operation, @RequestParam int page, @RequestParam int size)
            throws WrongOperationException {
        return calculationService.listContainsOp(operation, page, size).getContent();
    }

    @GetMapping("popular")
    public ResponseEntity<Double> popular() {
        return new ResponseEntity<>(calculationService.popularNumber(), HttpStatus.OK);
    }

}
