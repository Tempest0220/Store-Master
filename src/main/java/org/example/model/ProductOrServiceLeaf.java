package org.example.model;

public class ProductOrServiceLeaf extends ProductOrService implements ProductOrServiceComponent {
    // 建構子
    public ProductOrServiceLeaf(String identifier, String displayName, String description, double price) {
        super(identifier, displayName, description, price);
    }

    // 實作 getDisplayName() 方法
    @Override
    public String getDisplayName() {
        return this.displayName;
    }


}
