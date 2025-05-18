package org.example.pattern.decorator;

public class ConcreteComponent implements Component {

    @Override
    public void execute() {
        System.out.println("Executing ConcreteComponent");
    }
}
