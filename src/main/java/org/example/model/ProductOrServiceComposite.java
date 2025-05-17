package org.example.model;

import java.util.List;
import java.util.ArrayList;

// category
public class ProductOrServiceComposite implements ProductOrServiceComponent {
    private String identifier;
    private String displayName;
    private String description = "";

    private List<ProductOrServiceComponent> children = new ArrayList<>();

    // 建構子
    public ProductOrServiceComposite(String identifier, String displayName, String description) {
        this.identifier = identifier;
        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        StringBuilder displayNames = new StringBuilder();
        displayNames.append(this.displayName).append("\n");
        for (ProductOrServiceComponent child : children) {
            displayNames.append(child.getDisplayName()).append("\n");
        }
        return displayNames.toString();
    }

    public void add(ProductOrServiceComponent child) {
        children.add(child);
    }

}
