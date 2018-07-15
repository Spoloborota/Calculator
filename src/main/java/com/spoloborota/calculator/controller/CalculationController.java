package com.spoloborota.calculator.controller;

import com.spoloborota.calculator.entity.Calculation;
import com.spoloborota.calculator.service.ICalculationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/")
public class CalculationController {

    @Autowired
    private ICalculationService calculationService;

    @GetMapping("calculations")
    public ResponseEntity<List<Calculation>> getAllCalculations() {
        return new ResponseEntity<List<Calculation>>(calculationService.getAllCalculations(), HttpStatus.OK);
    }

    @PostMapping("calculate")
    public ResponseEntity<Double> calculateExpression(@RequestBody String expression) {
        return new ResponseEntity<Double>(calculationService.calculateExpression(expression), HttpStatus.CREATED);
    }


}
