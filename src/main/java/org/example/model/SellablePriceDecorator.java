package org.example.model;

public abstract class SellablePriceDecorator extends Sellable {
    protected Sellable wrappee;

    // Constructor
    public SellablePriceDecorator(Sellable wrappee) {
        super(wrappee.getId(), wrappee.getDisplayName(), wrappee.getPrice(), wrappee.getDescription());
        this.wrappee = wrappee;
    }

    public Sellable getWrappee() {
        return wrappee;
    }

}

    
