package org.example.model;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

public abstract class SellableGroup implements SellableComponent {
    private String groupName; // 組合名稱
    private String description; // 組合描述
    private Set<SellableComponent> items; // 組合中的項目列表(composite)

    // Constructor
    public SellableGroup(String groupName, String description) {
        this.groupName = groupName;
        this.description = description;
        this.items = new HashSet<>();
    }

    public void addItem(SellableComponent item) {
        items.add(item);
    }

    public void removeItem(SellableComponent item) {
        items.remove(item);
    }

    @Override
    public double getPrice() {
        if (items.isEmpty()) {
            System.out.println("No items in the group.");
        }
        return items.stream().mapToDouble(SellableComponent::getPrice).sum();
        
    }

    public Set<SellableComponent> getChildren() {
        return new HashSet<>(items);
    }

    public String getGroupName() {
        return groupName;
    }
}
