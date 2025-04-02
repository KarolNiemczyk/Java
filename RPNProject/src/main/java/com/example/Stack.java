package com.example;

public class Stack<T> {
    private T[] stackArray;
    private int size;
    private static final int INITIAL_CAPACITY = 10;

    public Stack() {
        stackArray = (T[]) new Object[INITIAL_CAPACITY];
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void push(T element) {
        if (size == stackArray.length) {
            resize();
        }
        stackArray[size++] = element;
    }

    public T pop() {
        if (isEmpty()) {
            throw new IllegalStateException("Stos jest pusty.");
        }
        return stackArray[--size];
    }

    public T peek() {
        if (isEmpty()) {
            throw new IllegalStateException("Stos jest pusty.");
        }
        return stackArray[size - 1];
    }

    private void resize() {
        T[] newStackArray = (T[]) new Object[stackArray.length * 2];  // Rzutowanie na T
        System.arraycopy(stackArray, 0, newStackArray, 0, stackArray.length);
        stackArray = newStackArray;
    }

    public int size() {
        return size;
    }
}
