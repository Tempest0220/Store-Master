package org.example;

import org.example.model.SellableGroup;
import org.example.model.cart.BuyingCart;
import org.example.model.cart.CartItem;
import org.example.model.discount.DiscountFactory;
import org.example.model.sellable.ExpirableSellable;
import org.example.model.PriceStrategy;
import org.example.model.Sellable;

public class Main {
    public static void main(String[] args) {
        // Example usage of the Composite and Sellable classes
     


        SellableGroup itemGroup = new SellableGroup("root", "Product List") {};
        SellableGroup fruitGroup = new SellableGroup("fruits", "Fruit Products") {};
        SellableGroup vegetableGroup = new SellableGroup("vegetables", "Vegetable Products") {};
        SellableGroup otherGroup = new SellableGroup("others", "Other Products") {};


        Sellable apple = new Sellable("apple", "Apple", "Fresh red apple", 1.0, DiscountFactory.createDiscountStrategy("10%off"), 10) {};
        Sellable banana = new Sellable("banana", "Banana", "Ripe yellow banana", 0.5, DiscountFactory.createDiscountStrategy("10%off"), 10) {};
        Sellable carrot = new Sellable("carrot", "Carrot", "Crunchy orange carrot", 0.3, DiscountFactory.createDiscountStrategy("10%off"), 10) {};
        Sellable potato = new Sellable("potato", "Potato", "Starchy brown potato", 0.2, DiscountFactory.createDiscountStrategy("10%off"), 10) {};
        Sellable iphone11 = new Sellable("iphone11", "iPhone 11", "Latest Apple iPhone", 999.99, DiscountFactory.createDiscountStrategy("none"), 5) {};
        Sellable samsungS20 = new Sellable("samsungS20", "Samsung S20", "Latest Samsung Galaxy", 899.99, DiscountFactory.createDiscountStrategy("none"), 5) {};
        ExpirableSellable milk = new ExpirableSellable("milk", "Milk", "Fresh milk", 1.5, DiscountFactory.createDiscountStrategy("20%off"), 10, "2023-12-31") {};


        // Add items to the respective groups
        fruitGroup.add(apple);
        fruitGroup.add(banana);
        fruitGroup.add(milk);
        vegetableGroup.add(carrot);
        vegetableGroup.add(potato);
        otherGroup.add(iphone11);
        otherGroup.add(samsungS20);
        // Add groups to the main item group
        itemGroup.add(fruitGroup);
        itemGroup.add(vegetableGroup);
        itemGroup.add(otherGroup);
        // Print the JSON representation of the item group
        System.out.println(itemGroup.toJson());

        System.out.println("-------------------");

        // BuyingCart example
        BuyingCart cart = new BuyingCart("My Cart", "Shopping Cart Description");
        cart.addItem(apple);
        cart.addItem(banana);
        cart.addItem(carrot);
        cart.addItem(potato);
        cart.addItem(iphone11);
        cart.addItem(samsungS20);
        cart.addItem(samsungS20);
        cart.addItem(milk);


        // Print the JSON representation of the cart
        System.out.println(cart.toJson());

        System.out.println("Total price of cart: " + cart.getPrice());

        

    }
}