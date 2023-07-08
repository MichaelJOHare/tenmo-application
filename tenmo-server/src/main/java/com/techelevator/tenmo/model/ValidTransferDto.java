package com.techelevator.tenmo.model;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;
import java.util.Objects;

public class ValidTransferDto {
    @Min(1)
    @Max(2)
    private int transferTypeId;
    @Min(1)
    @Max(3)
    private int transferStatusId;
    @Min(1000)
    private int accountFromId;
    @Min(1000)
    private int accountToId;
    @Positive(message = "Transfer amount must be higher greater than $0.00")
    private BigDecimal amount;

    public ValidTransferDto(int transferTypeId, int transferStatusId, int accountFromId, int accountToId, BigDecimal amount) {
        this.transferTypeId = transferTypeId;
        this.transferStatusId = transferStatusId;
        this.accountFromId = accountFromId;
        this.accountToId = accountToId;
        this.amount = amount;
    }

    public int getTransferTypeId() {
        return transferTypeId;
    }

    public void setTransferTypeId(int transferTypeId) {
        this.transferTypeId = transferTypeId;
    }

    public int getTransferStatusId() {
        return transferStatusId;
    }

    public void setTransferStatusId(int transferStatusId) {
        this.transferStatusId = transferStatusId;
    }

    public int getAccountFromId() {
        return accountFromId;
    }

    public void setAccountFromId(int accountFromId) {
        this.accountFromId = accountFromId;
    }

    public int getAccountToId() {
        return accountToId;
    }

    public void setAccountToId(int accountToId) {
        this.accountToId = accountToId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidTransferDto validTransferDto = (ValidTransferDto) o;
        return transferTypeId == validTransferDto.transferTypeId &&
                transferStatusId == validTransferDto.transferStatusId &&
                accountFromId == validTransferDto.accountFromId &&
                accountToId == validTransferDto.accountToId &&
                amount.equals(validTransferDto.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transferTypeId, transferStatusId, accountFromId, accountToId, amount);
    }

    @Override
    public String toString() {
        return "ValidTransferDto{" +
                "transferTypeId=" + transferTypeId +
                ", transferStatusId=" + transferStatusId +
                ", accountFromId=" + accountFromId +
                ", accountToId=" + accountToId +
                ", amount=" + amount +
                '}';
    }
}
