package org.example;

import org.example.model.SellableGroup;
import org.example.model.discount.DiscountFactory;
import org.example.model.PriceStrategy;
import org.example.model.Sellable;

public class Main {
    public static void main(String[] args) {
        // Example usage of the Composite and Sellable classes
     


        SellableGroup itemGroup = new SellableGroup("root", "Product List") {
            @Override
            public double getPrice() {
                return super.getPrice();
            }
        };

        SellableGroup fruitGroup = new SellableGroup("fruits", "Fruit Products") {
            @Override
            public double getPrice() {
                return super.getPrice();
            }
        };

        SellableGroup vegetableGroup = new SellableGroup("vegetables", "Vegetable Products") {
            @Override
            public double getPrice() {
                return super.getPrice();
            }
        };

        SellableGroup otherGroup = new SellableGroup("others", "Other Products") {
            @Override
            public double getPrice() {
                return super.getPrice();
            }
        };

        Sellable apple = new Sellable("apple", "Apple", "Fresh red apple", 1.0, DiscountFactory.createDiscountStrategy("10%off")) {};
        Sellable banana = new Sellable("banana", "Banana", "Ripe yellow banana", 0.5, DiscountFactory.createDiscountStrategy("10%off")) {};
        Sellable carrot = new Sellable("carrot", "Carrot", "Crunchy orange carrot", 0.3, DiscountFactory.createDiscountStrategy("10%off")) {};
        Sellable potato = new Sellable("potato", "Potato", "Starchy brown potato", 0.2, DiscountFactory.createDiscountStrategy("10%off")) {};
        Sellable iphone11 = new Sellable("iphone11", "iPhone 11", "Latest Apple iPhone", 999.99, DiscountFactory.createDiscountStrategy("none")) {};
        Sellable samsungS20 = new Sellable("samsungS20", "Samsung S20", "Latest Samsung Galaxy", 899.99, DiscountFactory.createDiscountStrategy("none")) {};


        // Add items to the respective groups
        fruitGroup.add(apple);
        fruitGroup.add(banana);
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

        

    }
}