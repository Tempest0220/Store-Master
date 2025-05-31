package org.example.model;

import java.util.ArrayList;

public abstract class SellableGroup extends Component {
    private String compositeName;
    private String compositeDescription;
    ArrayList<Component> components = new ArrayList<>();

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

}
