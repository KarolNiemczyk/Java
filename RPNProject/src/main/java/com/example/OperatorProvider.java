package com.example;

import java.util.List;

public class OperatorProvider {
    private final List<Operator> operators;

    public OperatorProvider(List<Operator> operators) {
        this.operators = operators;
    }

    public Operator getOperator(String symbol) {
        return operators.stream()
                .filter(op -> op.supports(symbol))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Nieobsługiwany operator: " + symbol));
    }
}
