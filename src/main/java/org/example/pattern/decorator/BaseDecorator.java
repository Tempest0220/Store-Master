package org.example.pattern.decorator;

public abstract class BaseDecorator implements Component {
    protected Component wrappee;

    // Constructor
    public BaseDecorator(Component wrappee) {
        this.wrappee = wrappee;
    }

    @Override
    public void execute() {
        // Default implementation
        wrappee.execute();
    }
}
