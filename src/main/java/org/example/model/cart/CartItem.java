package org.example.model.cart;

import org.example.model.PriceStrategy;
import org.example.model.Sellable;
import org.example.model.discount.DiscountFactory;
import org.example.model.sellable.ExpirableSellable;

public class CartItem {
    private Sellable sellable;
    private int quantity;

    public CartItem(Sellable sellable, int quantity) {
        this.sellable = sellable;
        this.quantity = quantity;
    }

    public CartItem(String id, String displayName, String description, double price, PriceStrategy priceStrategy) {
        this(new Sellable(id, displayName, description, price, priceStrategy, 0){}, 1);
    }

    public CartItem(Sellable sellable) {
        this(sellable, 1);
    }

    public int getQuantity() {
        return quantity;
    }

    public String getId() {
        return sellable.getId();
    }
    public String getDisplayName() {
        return sellable.getDisplayName();
    }
    public String getDescription() {
        return sellable.getDescription();
    }
    public Sellable getSellable() {
        return sellable;
    }
    public PriceStrategy getPriceStrategy() {
        return sellable.getPriceStrategy();
    }

    public void Plus() {
        this.quantity++;
    }

    public void Minus() {
        if (this.quantity > 0) {
            this.quantity--;
        }
    }


    public double getPrice() {
        return sellable.getPrice() * quantity; // Total price is base price times quantity
    }


    public String toJson() {
        return String.format("%s, \"quantity\": %d, \"totalPrice\": %.2f }",
            sellable.toJson().substring(0, sellable.toJson().lastIndexOf('}')), 
            quantity, 
            getPrice()
            ); // Include quantity in the JSON representation


    }

    

}
