package org.example.model.sellable;
import org.example.model.Sellable;
import org.example.model.PriceStrategy;

public class ExpirableSellable extends Sellable {
    private String expirationDate; // Expiration date in ISO format (e.g., "2023-10-31")

    public ExpirableSellable(String id, String displayName, String description, double price, PriceStrategy priceStrategy, int stock, String expirationDate) {
        super(id, displayName, description, price, priceStrategy, stock);
        this.expirationDate = expirationDate;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toJson() {
        return String.format(
                "{ \"id\": \"%s\", \"displayName\": \"%s\", \"description\": \"%s\", \"price\": %.2f, \"priceAfterDiscount\": %.2f, \"priceStrategy\": \"%s\", \"expirationDate\": \"%s\" }",
                id, displayName, description, price, getPrice(), priceStrategy.toString(), expirationDate
        );
    }
    
}
