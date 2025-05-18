package org.example.model;

public abstract class SellablePriceDecorator implements SellableComponent {
    protected Sellable wrappee;

    // Constructor
    public SellablePriceDecorator(Sellable wrappee) {
        this.wrappee = wrappee;
    }

    public Sellable getWrappee() {
        return wrappee;
    }

    @Override
    public abstract double getPrice();
}

    
