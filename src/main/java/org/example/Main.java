package org.example;

import org.example.model.SellableGroup;
import org.example.model.PriceStrategy;
import org.example.model.Sellable;

public class Main {
    public static void main(String[] args) {
        // Example usage of the Composite and Sellable classes
        PriceStrategy discountStrategy = basePrice -> basePrice * 0.9; // 10% discount

        Sellable product1 = new Sellable("1", "Apple", "Description of Product 1", 100.0, discountStrategy) {
            @Override
            public double getPrice() {
                return super.getPrice();
            }
        };

        Sellable product2 = new Sellable("2", "Banana", "Description of Product 2", 200.0, discountStrategy) {
            @Override
            public double getPrice() {
                return super.getPrice();
            }
        };

        Sellable product3 = new Sellable("3", "Cherry", "Description of Product 3", 300.0, discountStrategy) {
            @Override
            public double getPrice() {
                return super.getPrice();
            }
        };

        Sellable product4 = new Sellable("4", "Ice Cream", "Description of Product 4", 400.0, discountStrategy) {
            @Override
            public double getPrice() {
                return super.getPrice();
            }
        };



        SellableGroup composite = new SellableGroup("Composite 1", "Description of Composite 1") {
            @Override
            public double getPrice() {
                return super.getPrice();
            }
        };

        composite.add(product1);
        composite.add(product2);
        composite.add(product3);
        composite.add(product4);

        composite.setStrategy("1", p -> p); // Set a different strategy for product1 (20% discount)
        composite.setStrategy("2", p -> p); // Set a different strategy for product2 (20% discount)

        System.out.println("Total price of composite: " + composite.getPrice());

    }
}