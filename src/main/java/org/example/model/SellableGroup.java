package org.example.model;

import java.util.ArrayList;

public abstract class SellableGroup extends Component {
    protected String compositeName;
    protected String compositeDescription;
    protected ArrayList<Component> components = new ArrayList<>();

    public SellableGroup(String compositeName, String compositeDescription) {
        this.compositeName = compositeName;
        this.compositeDescription = compositeDescription;
    }

    @Override
    public double getPrice() {
        double totalPrice = 0.0;
        for (Component component : components) {
            totalPrice += component.getPrice(); // Sum up the prices of all components
        }
        return totalPrice; // Return the total price of the composite
    }

    public void add(Component component) {
        components.add(component);
    }

    public void remove(Component component) {
        components.remove(component);
    }

    public ArrayList<Component> getComponents() {
        return components;
    }

    public String getCompositeName() {
        return compositeName;
    }
    public String getCompositeDescription() {
        return compositeDescription;
    }

    @Override
    public String toJson() {
        // "groupName": "Mock Group",
        // "groupDescription": "This is a mock group of items for testing purposes.",
        // "items": [
        StringBuilder json = new StringBuilder();
        json.append(String.format("{ \"groupName\": \"%s\", \"groupDescription\": \"%s\", \"items\": [",
                compositeName, compositeDescription));
        for (int i = 0; i < components.size(); i++) {
            json.append(components.get(i).toJson());
            if (i < components.size() - 1) {
                json.append(", ");
            }
        }
        json.append("] }");
        return json.toString();
    }

    public Sellable findSellable(String id) {
        // 樹查找
        for (Component component : components) {
            if (component instanceof Sellable && ((Sellable) component).getId().equals(id)) {
                return (Sellable) component; // Return the Sellable component if found
            } else if (component instanceof SellableGroup) {
                Sellable found = ((SellableGroup) component).findSellable(id); // Recursive call for nested composites
                if (found != null) {
                    return found; // Return if found in nested composite
                }
            }
        }
        return null; // Return null if not found
    }

    public void setStrategy(String id, PriceStrategy strategy) {
        Sellable sellable = findSellable(id);
        if (sellable != null) {
            sellable.setPriceStrategy(strategy); // Set the price strategy for the found Sellable
        } else {
            System.out.println("Sellable with ID " + id + " not found.");
        }
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
        SellableGroup group = new SellableGroup("root", "product list") {
            @Override
            public double getPrice() {
                return super.getPrice();
            }
        };

        group.add(new TempProduct("1", "Apple", "A juicy apple", 100.0, tenPercentOff));
        group.add(new TempProduct("2", "Banana", "A ripe banana", 200.0, tenPercentOff));
        group.add(new TempProduct("3", "Cherry", "A sweet cherry", 300.0, tenPercentOff));
        group.add(new TempProduct("4", "Ice Cream", "A cold ice cream", 400.0, tenPercentOff));

        // Add components and test the functionality
        // Note: You would typically add Sellable instances to this group
        System.out.println(group.toJson());
    }

}
