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

    @Override
    public String getDescription() {
        return super.getDescription() + " (Half Price)"; // 返回包裝物件的描述，並加上"Half Price"
    }
    
}
