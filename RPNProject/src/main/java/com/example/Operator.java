package com.example;

public interface Operator {
    boolean supports(String operator);
    int apply(int a, int b);
}