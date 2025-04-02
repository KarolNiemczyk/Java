package com.example;


import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class StackTest {
    @Test
    public void testPushAndPop() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    public void testResize() {
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < 12; i++) {
            stack.push(i);
        }
        assertEquals(11, stack.pop());
        assertEquals(10, stack.pop());
    }

    @Test
    public void testPopEmptyStack() {
        Stack<Integer> stack = new Stack<>();
        assertThrows(IllegalStateException.class, stack::pop);
    }

    @Test
    public void testPeekEmptyStack() {
        Stack<Integer> stack = new Stack<>();
        assertThrows(IllegalStateException.class, stack::peek);
    }

    @Test
    public void testIsEmpty() {
        Stack<Integer> stack = new Stack<>();
        assertTrue(stack.isEmpty());
        stack.push(1);
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test
    public void testSizeAfterPushAndPop() {
        Stack<Integer> stack = new Stack<>();
        assertEquals(0, stack.size());
        stack.push(10);
        assertEquals(1, stack.size());
    }

    @Test
    public void testPushNull() {
        Stack<Integer> stack = new Stack<>();
        stack.push(null);
        assertNull(stack.pop());
    }

    @Test
    public void testOrderPreserved() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.push(3);
        assertEquals(3, stack.pop());
        assertEquals(2, stack.pop());
        assertEquals(1, stack.pop());
    }

    @Test
    public void testMultipleResizes() {
        Stack<Integer> stack = new Stack<>();
        for (int i = 0; i < 50; i++) {
            stack.push(i);
        }
        assertEquals(50, stack.size());
        for (int i = 49; i >= 0; i--) {
            assertEquals(i, stack.pop());
        }
    }

    @Test
    public void testPeekDoesNotRemoveElement() {
        Stack<Integer> stack = new Stack<>();
        stack.push(5);
        assertEquals(5, stack.peek());
        assertEquals(5, stack.peek());
        assertEquals(1, stack.size());
    }

    @Test
    public void testClearStackAndReuse() {
        Stack<Integer> stack = new Stack<>();
        stack.push(1);
        stack.push(2);
        stack.pop();
        stack.pop();
        assertTrue(stack.isEmpty());
        stack.push(3);
        assertEquals(3, stack.pop());
    }

    @Test
    public void testDifferentDataTypes() {
        Stack<String> stack = new Stack<>();
        stack.push("Hello");
        stack.push("World");
        assertEquals("World", stack.pop());
        assertEquals("Hello", stack.pop());
    }

    @Test
    public void testNegativeAndZeroValues() {
        Stack<Integer> stack = new Stack<>();
        stack.push(-5);
        stack.push(0);
        assertEquals(0, stack.pop());
        assertEquals(-5, stack.pop());
    }
}
