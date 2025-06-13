package com.store.framework;

// Leaf node for individual products
public class ProductItem implements ProductComponent {
    private String name;
    private double price;
    private int quantity;
    private Discount discount;

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
        // 因為不知道GUI怎麼調用，所以price不能隨quantity、customer變化
//            @Override
//            public double getPrice(double price, int quantity, Customer customer) {
//                return price;
//            }
//        };
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

    @Override
    public String toString() {
        return name + "   (qty=" + quantity + ", price=" + getPrice() + ")";
    }
}
