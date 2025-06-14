package com.store.framework;

public interface Discount {
    double getPrice(double price);
    default String getName(){return "";}
}
