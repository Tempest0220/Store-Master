package org.example.model;


// 假設食物擁有屬性：有效期
public class Food extends Sellable {
    private String expirationDate; // 食物的有效期(假設)

    // Constructor
    public Food(String id, String displayName, double price, String description, String expirationDate) {
        super(id, displayName, price, description);
        this.expirationDate = expirationDate;
    }
    public Food(String id, double price, String expirationDate) {
        this(id, id, price, "", expirationDate);
    }

    public String getExpirationDate() {
        return expirationDate;
    }
}
    

