package com.techelevator.tenmo.model;

public class Account {
    private Balance balance;
    private int accountId;
    private int userId;

    public Account() { }
    public Account(Balance balance, int accountId, int userId) {
        this.balance = balance;
        this.accountId = accountId;
        this.userId = userId;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Balance getBalance() {
        return balance;
    }

    public void setBalance(Balance balance) {
        this.balance = balance;
    }
}
