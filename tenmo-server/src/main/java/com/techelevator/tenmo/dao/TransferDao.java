package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.ValidTransferDto;

import java.util.List;

public interface TransferDao {
    Transfer getTransferById(int transferId);
    Transfer createTransfer(ValidTransferDto transfer);
    List<Transfer> getTransfers();
}
