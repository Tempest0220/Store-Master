package org.example.pattern.decorator;



public class ConcreteDecorator extends BaseDecorator {

    public ConcreteDecorator(Component wrappee) {
        super(wrappee);
    }

    @Override
    public void execute() {
        System.out.println("裝飾器的功能");
        super.execute();
        
    }

}
