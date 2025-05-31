package org.example.model.cart;

import java.util.ArrayList;

import org.example.model.Component;
import org.example.model.PriceStrategy;
import org.example.model.Sellable;
import org.example.model.SellableGroup;


public class BuyingCart extends Component {
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
    public void removeItem(Sellable item) {
        items.removeIf(cartItem -> cartItem.getId().equals(item.getId()));
    }
    public ArrayList<CartItem> getItems() {
        return items;
    }
    @Override
    public double getPrice() {
        double totalPrice = 0.0;
        for (CartItem item : items) {
            totalPrice += item.getPrice(); // Sum up the prices of all items
        }
        return totalPrice; // Return the total price of the cart
    }
    @Override
    public String toJson() {
        StringBuilder json = new StringBuilder();
        json.append(String.format("{ \"cartName\": \"%s\", \"cartDescription\": \"%s\", \"items\": [",
                cartName, cartDescription));
        for (int i = 0; i < items.size(); i++) {
            json.append(items.get(i).toJson());
            if (i < items.size() - 1) {
                json.append(", ");
            }
        }
        json.append("]");
        json.append(String.format(", \"totalPrice\": %.2f }", getPrice()));
        return json.toString();
    }    

    public static void main(String[] args) {
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

        class TempProduct extends Sellable {
            public TempProduct(String id, String displayName, String description, double price, PriceStrategy priceStrategy) {
                super(id, displayName, description, price, priceStrategy);
            }
        }

        // Example usage
        BuyingCart cart = new BuyingCart("My Cart", "Shopping Cart Description");
        CartItem item1 = new CartItem(new TempProduct("1", "Apple", "Fresh Apple", 1.0, tenPercentOff));
        CartItem item2 = new CartItem(new TempProduct("2", "Banana", "Fresh Banana", 0.5, tenPercentOff));
        CartItem item3 = new CartItem(new TempProduct("3", "Cherry", "Fresh Cherry", 2.0, tenPercentOff));
        cart.addItem(item1);
        cart.addItem(item2);
        cart.addItem(item3);

        item1.Plus(); // Increase quantity of item1
        System.out.println("Total price of cart: " + cart.getPrice());
        System.out.println(cart.toJson()); // Output the JSON representation of the cart

    }

}
