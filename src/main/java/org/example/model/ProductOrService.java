package org.example.model;
import org.example.model.*;

public abstract class ProductOrService {
    protected String identifier;
    protected String displayName;
    protected String description = "";
    protected double price;

    // 建構子
    public ProductOrService(String identifier, String displayName, String description, double price) {
        this.identifier = identifier;
        this.displayName = displayName;
        this.price = price;
    }
}
