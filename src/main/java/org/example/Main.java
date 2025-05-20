package org.example;
import org.example.model.Sellable;
import org.example.model.SellableComponent;
import org.example.model.SellableGroup;
import org.example.model.SellablePriceDecorator;
import org.example.model.Food;
import org.example.model.HalfPrice;
import org.example.pattern.*;


// Composite Pattern Test
public class Main {
    public static void main(String[] args) {
        Sellable food = new Food("Food 1", 10.0, "2023-12-31");
        Sellable halfPriceFood = new HalfPrice(food);
        Sellable halfhalfPriceFood = new HalfPrice(halfPriceFood);
        Sellable unknownSellable = new Sellable("Unknown", 0.0) {};




        SellableGroup group1 = new SellableGroup("Group 1", "This is group 1") {};

        // showItemDetails(halfPriceFood);

        group1.addItem(food);
        group1.addItem(halfPriceFood);
        group1.addItem(halfhalfPriceFood);
        group1.addItem(unknownSellable);


        for (SellableComponent item : group1.getChildren()) {
            showItemDetails(item);
        }

    }

    private static void showItemDetails(SellableComponent item) {
        if (!(item instanceof SellableGroup)) {
            Sellable sellable = (Sellable) item;
            System.out.println("ID: " + sellable.getId());
            System.out.println("Display Name: " + sellable.getDisplayName());
            System.out.println("Description: " + sellable.getDescription());
            System.out.println("Price: " + sellable.getPrice());
            System.out.println("-----------------------------");
        } else if (item instanceof SellableGroup) {
            for (SellableComponent child : ((SellableGroup) item).getChildren()) {
                showItemDetails(child);
            }
        }
    }

}
