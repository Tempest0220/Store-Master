package com.store.real.restaurant;

import com.store.framework.*;

import java.util.Map;

/**
 * 套餐 + 會員折扣規則：<br/>
 * 1. 若同一張訂單同時包含「前菜、主食、甜點」各 ≥1 份，<br/>
 *    以可湊成的組數為單位，這三項菜色打 9 折 (10% off)。<br/>
 * 2. 若有會員，再於整筆金額額外打 9 折。<br/>
 */
public class RestaurantSalesRule implements SalesRule {

    private RestaurantStore store;
    public RestaurantSalesRule(RestaurantStore store){
        this.store = store;
    }

    public void setStore(RestaurantStore store){
        this.store = store;
    }

    @Override
    public double applySale(Map<ProductComponent,Integer> items, Customer member){
        double gross    = 0;
        int starterCnt  = 0;
        int mainCnt     = 0;
        int dessertCnt  = 0;
        double starterSum = 0, mainSum = 0, dessertSum = 0;

        for(var e: items.entrySet()){
            ProductComponent p = e.getKey();
            int qty = e.getValue();
            double subTotal = p.getPrice()*qty;
            gross += subTotal;

            String cat = store.getCategoryOf(p.getName());
            if("前菜".equals(cat)){
                starterCnt += qty;
                starterSum += subTotal;
            }else if("主食".equals(cat)){
                mainCnt += qty;
                mainSum += subTotal;
            }else if("甜點".equals(cat)){
                dessertCnt += qty;
                dessertSum += subTotal;
            }
        }

        int setCount = Math.min(starterCnt, Math.min(mainCnt, dessertCnt));
        double oneStarter   = starterSum / Math.max(starterCnt,1);
        double oneMain      = mainSum    / Math.max(mainCnt,1);
        double oneDessert   = dessertSum / Math.max(dessertCnt,1);
        double setPriceSum  = oneStarter + oneMain + oneDessert;

        double setDiscount = setCount * setPriceSum * 0.10; // 10% off 套餐
        double afterSet    = gross - setDiscount;

        double memberDiscount = (member!=null) ? afterSet*0.10 : 0; // 會員再打 9 折
        if(member!=null) member.addPoints((int)(afterSet - memberDiscount));

        return afterSet - memberDiscount;
    }

    /* 單品版本維持相容（不套套餐規則） */
    @Override
    public double applySale(ProductComponent p, int qty, Customer c){
        return p.getPrice()*qty;
    }
}
