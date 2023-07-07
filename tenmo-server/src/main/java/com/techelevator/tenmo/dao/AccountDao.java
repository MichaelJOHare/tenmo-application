package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;

public interface AccountDao {
    Balance getBalance(String username);
    Account getAccountByUserId(int userId);
    Account getAccountById(int id);
    void updateAccountBalance(Account account);
    String getUsernameByAccountId(int accountId);
}
