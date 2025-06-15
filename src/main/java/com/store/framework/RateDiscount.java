package com.store.framework;

/**
 * 一種常見的單品折價方式，原價成以一定比例等於打折後的價格
 */
public class RateDiscount implements Discount{
    private double rate;
    public RateDiscount(double rate){
        this.rate = rate;
    }
    public void setRate(double rate){
        this.rate = rate;
    }
    @Override
    public double getPrice(double price) {
        return price * rate;
    }

    @Override
    public String getName(){
        return String.format("%.0f%% off", (1-rate) * 100);
    }
}
