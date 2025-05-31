package org.example.model;



public abstract class Sellable extends Component {
    private String id;
    private String displayName;
    private String description;
    private double price; // base price
    private PriceStrategy priceStrategy; // price strategy, a function field


    public Sellable(String id, String displayName, String description, double price, PriceStrategy priceStrategy) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.priceStrategy = priceStrategy;
    }

    public double getPrice() {
        return priceStrategy.getPrice(price);
    }
    public String getId() {
        return id;
    }
    public String getDisplayName() {
        return displayName;
    }
    public String getDescription() {
        return description;
    }

    public void setPriceStrategy(PriceStrategy priceStrategy) {
        this.priceStrategy = priceStrategy;
    }


    public static void main(String[] args) {
        // Example usage
        PriceStrategy discountStrategy = basePrice -> basePrice * 0.9; // 10% discount
        Sellable product = new Sellable("1", "Example Product", "This is an example product.", 100.0, discountStrategy) {
            // Anonymous class for abstract Sellable
        };

        System.out.println("Product ID: " + product.id);
        System.out.println("Display Name: " + product.displayName);
        System.out.println("Description: " + product.description);
        System.out.println("Price after strategy: " + product.getPrice());
    }

}
