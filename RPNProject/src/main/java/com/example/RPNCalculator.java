package com.example;

public class RPNCalculator {
    private final Stack<Integer> stack;
    private final OperatorProvider operatorProvider;

    public RPNCalculator(Stack<Integer> stack, OperatorProvider operatorProvider) {
        this.stack = stack;
        this.operatorProvider = operatorProvider;
    }

    public int evaluate(String expression) {
        String[] tokens = expression.split("\\s+");

        for (String token : tokens) {
            try {
                stack.push(Integer.parseInt(token));
            } catch (NumberFormatException e) {
                if (stack.size() < 2) {
                    throw new IllegalStateException("Invalid RPN expression: Not enough operands.");
                }
                int b = stack.pop();
                int a = stack.pop();
                int result = operatorProvider.getOperator(token).apply(a, b);
                stack.push(result);
            }
        }

        if (stack.size() != 1) {
            throw new IllegalStateException("Invalid RPN expression: More than one result.");
        }

        return stack.peek();
    }
}


