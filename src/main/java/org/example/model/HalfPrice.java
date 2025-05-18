package org.example.model;

public class HalfPrice extends SellablePriceDecorator {
    // Constructor
    public HalfPrice(Sellable wrappee) {
        super(wrappee);
    }

    @Override
    public double getPrice() {
        return wrappee.getPrice() / 2; // 返回包裝物件的價格的一半
    }
    
}
