package com.anyMind.application.wallet;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;

public class WalletResponce {
    private ZonedDateTime dateTime;
    private double amount;

    public ZonedDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(ZonedDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public WalletResponce(ZonedDateTime dateTime, double amount) {
        this.dateTime = dateTime;
        this.amount = amount;
    }
}
