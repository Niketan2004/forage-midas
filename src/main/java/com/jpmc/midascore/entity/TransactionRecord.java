package com.jpmc.midascore.entity;

import jakarta.persistence.*;

@Entity
public class TransactionRecord {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Long transactionId;
    @ManyToOne
    private UserRecord sender;
    @ManyToOne
    private UserRecord reciever;
    private Float amount;
    private Float incentive;
    public TransactionRecord() {
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
    }

    public UserRecord getSender() {
        return sender;
    }

    public void setSender(UserRecord sender) {
        this.sender = sender;
    }

    public UserRecord getReciever() {
        return reciever;
    }

    public void setReciever(UserRecord reciever) {
        this.reciever = reciever;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public Float getIncentive() {
        return incentive;
    }

    public void setIncentive(Float incentive) {
        this.incentive = incentive;
    }

    public TransactionRecord(Long transactionId, UserRecord sender, UserRecord reciever, Float amount, Float incentive) {
        this.transactionId = transactionId;
        this.sender = sender;
        this.reciever = reciever;
        this.amount = amount;
        this.incentive = incentive;
    }
}
