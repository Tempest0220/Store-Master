package org.example.Frame;

public class ProductNotEnoughException extends RuntimeException {
    public ProductNotEnoughException(String message) {
        super(message);
    }
}
