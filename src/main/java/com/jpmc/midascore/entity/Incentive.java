package com.jpmc.midascore.entity;

public class Incentive {
    private Float amount;

    public Incentive() {
    }

    public Incentive(Float balance) {
        this.amount = balance;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }
}
