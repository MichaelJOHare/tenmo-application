package com.techelevator.tenmo.model;

import java.math.BigDecimal;
import java.util.Objects;

public class Balance {
    private BigDecimal balance;

    public BigDecimal getBalance() {
        return balance;
    }
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Balance otherBalance = (Balance) o;
        return balance.equals(otherBalance.balance);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance);
    }

    @Override
    public String toString() {
        return "Balance{" +
                "balance=" + balance +
                '}';
    }
}
