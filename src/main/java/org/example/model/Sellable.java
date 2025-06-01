package org.example.model;



public abstract class Sellable extends Component {
    protected String id;
    protected String displayName;
    protected String description;
    protected double price; // base price
    protected PriceStrategy priceStrategy; // price strategy, a function field
    protected int stock;

    public Sellable(String id, String displayName, String description, double price, PriceStrategy priceStrategy, int stock) {
        this.id = id;
        this.displayName = displayName;
        this.description = description;
        this.price = price;
        this.priceStrategy = priceStrategy;
        this.stock = stock;
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
    public PriceStrategy getPriceStrategy() {
        return priceStrategy;
    }
    public int getStock() {
        return stock;
    }


    public void setStock(int stock) {
        this.stock = stock;
    }
    public void setPriceStrategy(PriceStrategy priceStrategy) {
        this.priceStrategy = priceStrategy;
    }


    public String toJson() {
        // {
        //     "id": 1,
        //     "displayName": "Mock Item 1",
        //     "description": "This is a mock item for testing purposes.",
        //     "price": 19.99,
        //     "priceAfterDiscount": 17.99,
        //     "priceStrategy": "10% off"
        // },
        return String.format(
                "{ \"id\": \"%s\", \"displayName\": \"%s\", \"description\": \"%s\", \"price\": %.2f, \"priceAfterDiscount\": %.2f, \"priceStrategy\": \"%s\", \"stock\": %d }",
                id, displayName, description, price, getPrice(), priceStrategy.toString(), stock
        );
    }


}
