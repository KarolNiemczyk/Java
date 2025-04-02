package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class RPNCalculatorTest {

    @Test
    public void testSimpleAddition() {
        // Tworzymy stack i operatorProvider
        Stack<Integer> stack = new Stack<>();
        OperatorProvider operatorProvider = new OperatorProvider(List.of(new AddOperator()));

        // Tworzymy kalkulator z odpowiednimi zależnościami
        RPNCalculator calculator = new RPNCalculator(stack, operatorProvider);
        assertEquals(5, calculator.evaluate("2 3 +"));
    }

    @Test
    public void testSubtraction() {
        Stack<Integer> stack = new Stack<>();
        OperatorProvider operatorProvider = new OperatorProvider(List.of(new SubtractOperator()));
        RPNCalculator calculator = new RPNCalculator(stack, operatorProvider);
        assertEquals(1, calculator.evaluate("3 2 -"));
    }

    @Test
    public void testMultiplication() {
        Stack<Integer> stack = new Stack<>();
        OperatorProvider operatorProvider = new OperatorProvider(List.of(new MultiplyOperator()));
        RPNCalculator calculator = new RPNCalculator(stack, operatorProvider);
        assertEquals(6, calculator.evaluate("2 3 *"));
    }

    @Test
    public void testComplexExpression() {
        Stack<Integer> stack = new Stack<>();
        OperatorProvider operatorProvider = new OperatorProvider(List.of(new AddOperator(), new MultiplyOperator()));
        RPNCalculator calculator = new RPNCalculator(stack, operatorProvider);
        assertEquals(20, calculator.evaluate("2 3 + 4 *"));
    }

    @Test
    public void testWrongOperator() {
        Stack<Integer> stack = new Stack<>();
        OperatorProvider operatorProvider = new OperatorProvider(List.of(new AddOperator(), new SubtractOperator(), new MultiplyOperator()));
        RPNCalculator calculator = new RPNCalculator(stack, operatorProvider);
        assertThrows(IllegalArgumentException.class, () -> calculator.evaluate("2 3 + 4 x"));
    }

    @Test
    public void testNoEnoughNumbers() {
        Stack<Integer> stack = new Stack<>();
        OperatorProvider operatorProvider = new OperatorProvider(List.of(new AddOperator(), new SubtractOperator(), new MultiplyOperator()));
        RPNCalculator calculator = new RPNCalculator(stack, operatorProvider);
        assertThrows(IllegalStateException.class, () -> calculator.evaluate("2 +"));
    }

    @Test
    public void testMultipleOperators() {
        Stack<Integer> stack = new Stack<>();
        OperatorProvider operatorProvider = new OperatorProvider(List.of(new AddOperator(), new SubtractOperator(), new MultiplyOperator()));
        RPNCalculator calculator = new RPNCalculator(stack, operatorProvider);
        assertEquals(14, calculator.evaluate("5 1 2 + 4 * + 3 -"));
    }
    @Test
    public void testTooManyOperands() {
        Stack<Integer> stack = new Stack<>();
        OperatorProvider operatorProvider = new OperatorProvider(List.of(new AddOperator(), new SubtractOperator(), new MultiplyOperator()));
        RPNCalculator calculator = new RPNCalculator(stack, operatorProvider);
        assertThrows(IllegalStateException.class, () -> calculator.evaluate("3 4 5 +"));
    }

}
