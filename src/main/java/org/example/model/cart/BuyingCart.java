package org.example.model.cart;

import java.util.ArrayList;

import org.example.model.Component;
import org.example.model.PriceStrategy;
import org.example.model.Sellable;
import org.example.model.SellableGroup;
import org.example.model.discount.DiscountFactory;
import org.example.model.sellable.ExpirableSellable;


public class BuyingCart {
    String cartName;
    String cartDescription;
    ArrayList<CartItem> items = new ArrayList<>();

    public BuyingCart(String cartName, String cartDescription) {
        this.cartName = cartName;
        this.cartDescription = cartDescription;
    }
    public void addItem(Sellable item) {
        // 如果購物車裡面已經有這個商品，則增加數量
        for (CartItem existingItem : items) {
            if (existingItem.getId().equals(item.getId())) {
                existingItem.Plus(); // Increase quantity if item already exists
                return;
            }
        }
        items.add(new CartItem(item));
    }

    public void addItem(CartItem item) {
        // 如果購物車裡面已經有這個商品，則增加數量
        for (CartItem existingItem : items) {
            if (existingItem.getId().equals(item.getId())) {
                existingItem.Plus(); // Increase quantity if item already exists
                return;
            }
        }
        items.add(item);
    }
    public void removeItem(Sellable item) {
        items.removeIf(cartItem -> cartItem.getId().equals(item.getId()));
    }
    public ArrayList<CartItem> getItems() {
        return items;
    }

    public double getPrice() {
        double totalPrice = 0.0;
        for (CartItem item : items) {
            totalPrice += item.getPrice(); // Sum up the prices of all items
        }
        return totalPrice; // Return the total price of the cart
    }

    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append(String.format("{ \"cartName\": \"%s\", \"cartDescription\": \"%s\", \"items\": [",
                cartName, cartDescription));

        for (int i = 0; i < items.size(); i++) {
            CartItem item = items.get(i);
            json.append(item.toJson());

            if (i < items.size() - 1) json.append(", ");
            
        }
        json.append("]");
        json.append(String.format(", \"totalPrice\": %.2f }", getPrice()));
        return json.toString();
    }    

}
