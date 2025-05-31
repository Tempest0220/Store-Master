package org.example.model.cart;

import org.example.model.PriceStrategy;
import org.example.model.Sellable;

public class CartItem extends Sellable {
    private int quantity;

    public CartItem(String id, String displayName, String description, double price, int quantity, PriceStrategy priceStrategy) {
        super(id, displayName, description, price, priceStrategy);
        this.quantity = quantity;
    }

    public CartItem(String id, String displayName, String description, double price, PriceStrategy priceStrategy) {
        super(id, displayName, description, price, priceStrategy);
        this.quantity = 1; // Default quantity is 1
    }

    public CartItem(Sellable sellable) {
        super(sellable.getId(), sellable.getDisplayName(), sellable.getDescription(), sellable.getPrice(), sellable.getPriceStrategy());
        this.quantity = 1;
    }

    public int getQuantity() {
        return quantity;
    }

    public void Plus() {
        this.quantity++;
    }

    public void Minus() {
        if (this.quantity > 0) {
            this.quantity--;
        }
    }

    @Override
    public double getPrice() {
        return super.getPrice() * quantity; // Total price is base price times quantity
    }

    @Override
    public String toJson() {
        // Include quantity in the JSON representation
        return String.format(
                "{ \"id\": \"%s\", \"displayName\": \"%s\", \"description\": \"%s\", \"price\": %.2f, \"priceStrategy\": \"%s\", \"quantity\": %d, \"totalPrice\": %.2f }",
                id, displayName, description, price, priceStrategy.toString(), quantity, getPrice()
        );
    }

    public static void main(String[] args) {
        // Example usage of CartItem
        PriceStrategy tenPercentOff = new PriceStrategy() {
            @Override
            public double getPrice(double basePrice) {
                return basePrice * 0.9; // 10% discount
            }
            @Override
            public String toString() {
                return "10% Discount Strategy";
            }
        };
        CartItem item = new CartItem("1", "Mock Item 1", "This is a mock item for testing purposes.", 19.99, tenPercentOff);
        System.out.println(item.toJson()); // Output the JSON representation of the cart item
        item.Plus(); // Increase quantity
        System.out.println(item.toJson()); // Output updated JSON representation
        item.Minus(); // Decrease quantity
        System.out.println(item.toJson()); // Output updated JSON representation
    }

}
