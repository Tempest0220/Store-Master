package com.store.framework;

/**
 * 葉節點
 */
public class ProductItem implements ProductComponent {
    private String name;
    private double price;
    private int quantity;
    protected Discount discount;

    public ProductItem(String name, double price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discount = new Discount() {
            @Override
            public double getPrice(double price) {
                return price;
            }
        };
    }

    public ProductItem(String name, double price, int quantity, Discount discount) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.discount = discount;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return discount.getPrice(this.price);
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /* ★ 新增：調整售價時用 */
    public void setPrice(double price) {
        this.price = price;
    }

    public void setDiscount(Discount discount) {
        this.discount = discount;
    }

    @Override
    public String toString() {
        if (discount.getName().isEmpty()){
            return name + "   (qty=" + quantity + ", price=" + getPrice() + ")";
        }else{
            return name + "   (qty=" + quantity + ", price=" + getPrice() + "(" + discount.getName() + "))";
        }

    }
}
