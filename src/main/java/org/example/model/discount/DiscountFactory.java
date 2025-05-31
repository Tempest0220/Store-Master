package org.example.model.discount;

import org.example.model.PriceStrategy;


public class DiscountFactory {

    public static PriceStrategy createDiscountStrategy(String discountType) {
        switch (discountType.toLowerCase()) {
            case "10%off":
                return new PriceStrategy() {
                    @Override
                    public double getPrice(double basePrice) {
                        return basePrice * 0.9; // 10% discount
                    }

                    @Override
                    public String toString() {
                        return "10% off";
                    }
                };
            case "20%off":
                return new PriceStrategy() {
                    @Override
                    public double getPrice(double basePrice) {
                        return basePrice * 0.8; // 20% discount
                    }

                    @Override
                    public String toString() {
                        return "20% off";
                    }
                };
            case "none":
                return new PriceStrategy() {
                    @Override
                    public double getPrice(double basePrice) {
                        return basePrice; // No discount
                    }

                    @Override
                    public String toString() {
                        return "No Discount";
                    }
                };
            default:
                throw new IllegalArgumentException("Unknown discount type: " + discountType);
        }
    }
}