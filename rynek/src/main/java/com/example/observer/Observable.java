package com.example.observer;

public interface Observable {
    void addObserver(Observer obs);
    void notifyObservers();
}