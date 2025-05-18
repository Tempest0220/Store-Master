package org.example.pattern.decorator;

public class Main {
    public static void main(String[] args) {
        Component decorator = new ConcreteDecorator(new ConcreteComponent());
        decorator.execute();
    }
    
}
