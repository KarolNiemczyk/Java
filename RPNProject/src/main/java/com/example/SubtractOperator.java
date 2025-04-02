package com.example;

public class SubtractOperator implements Operator {
    @Override
    public boolean supports(String operator) {
        return "-".equals(operator);
    }

    @Override
    public int apply(int a, int b) {
        return a - b;
    }
}