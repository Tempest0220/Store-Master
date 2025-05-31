package org.example.model;



public abstract class Sellable extends Component {
    protected String id;
    protected String displayName;
    protected String description;
    protected double price; // base price
    protected PriceStrategy priceStrategy; // price strategy, a function field


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
    public PriceStrategy getPriceStrategy() {
        return priceStrategy;
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
                "{ \"id\": \"%s\", \"displayName\": \"%s\", \"description\": \"%s\", \"price\": %.2f, \"priceAfterDiscount\": %.2f, \"priceStrategy\": \"%s\" }",
                id, displayName, description, price, getPrice(), priceStrategy.toString()
        );
    }


    public static void main(String[] args) {
        // Example usage
        PriceStrategy discountStrategy = new PriceStrategy() {
            @Override
            public double getPrice(double basePrice) {
                return basePrice * 0.9; // 10% discount
            }

            @Override
            public String toString() {
                return "10% Discount Strategy";
            }
            
        };
        Sellable product = new Sellable("1", "Example Product", "This is an example product.", 100.0, discountStrategy) {
            // Anonymous class for abstract Sellable
        };

        System.out.println(product.toJson());


    }

}
