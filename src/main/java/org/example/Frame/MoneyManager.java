package org.example.Frame;

import java.time.LocalDateTime;

public interface MoneyManager {
    void addReceivingLog(int[] productIds, int price);
    void addSalesLog(int[] productIds, int price);
    int getEarn(LocalDateTime from, LocalDateTime to);
}
