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
        Food food = new Food("Food 1", 10.0, "2023-12-31");
        HalfPrice halfPriceFood = new HalfPrice(food);
        // TODO
        // 處理只能裝飾Sellable的問題
        // 還有是不是所有Sellable的子類可以賦值到Sellable的問題,會不會丟失類型信息
        // 逆變只是確保類型安全,不會丟失類型信息，可以用instanceof來檢查,然後強制轉型
        Sellable unknownSellable = new Sellable("Unknown", 0.0) {};


        SellableGroup group1 = new SellableGroup("Group 1", "This is group 1") {};

        group1.addItem(halfPriceFood);
        group1.addItem(unknownSellable);

        System.out.println(group1.getGroupName());
        for (SellableComponent item : group1.getChildren()) {
            if (item instanceof SellablePriceDecorator) {
                Sellable discountItem = (Sellable) ((SellablePriceDecorator) item).getWrappee();

                // 混亂
                showSellableInfo(discountItem);
            }
            else {
                showSellableInfo((Sellable) item);
            }
        }


    }

    public static void showSellableInfo(Sellable item) {
        
        switch (item.getClass().getSimpleName()) {
            case "Food":
                Food food = (Food) item;
                System.out.println(item.getDisplayName() + ": " + "$" + item.getPrice() + " (Expiration Date: " + food.getExpirationDate() + ")");
                break;
            default:
                System.out.println(item.getDisplayName() + ": " + "$" + item.getPrice());
                break;
        }
    }
}
