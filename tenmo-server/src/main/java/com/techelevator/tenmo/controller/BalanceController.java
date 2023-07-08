package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferStatusDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;

/**
 * Controller to handle getting and updating balance
 */
@RestController
@RequestMapping("/balance")
@PreAuthorize("isAuthenticated()")
public class BalanceController {
    private final AccountDao accountDao;
    private final TransferStatusDao transferStatusDao;
    public BalanceController(AccountDao accountDao, TransferStatusDao transferStatusDao) {
        this.accountDao = accountDao;
        this.transferStatusDao = transferStatusDao;
    }
    @RequestMapping(path = "", method = RequestMethod.GET)
    public Balance get(Principal principal) {
        String username = principal.getName();
        return accountDao.getBalance(username);
    }
    @RequestMapping(path = "/account/{id}", method = RequestMethod.PUT)
    public void updateAccountBalance(@RequestBody Transfer transfer) {
        Account accountTo = accountDao.getAccountById(transfer.getAccountToId());
        Account accountFrom = accountDao.getAccountById(transfer.getAccountFromId());
        BigDecimal amountTransferred = transfer.getAmount();
        if (transfer.getTransferStatusId() == transferStatusDao.getTransferStatusByDescription("Approved").getTransferStatusId()) {
            accountFrom.getBalance().setBalance(accountFrom.getBalance().getBalance().subtract(amountTransferred));
            accountTo.getBalance().setBalance(accountTo.getBalance().getBalance().add(amountTransferred));
            accountDao.updateAccountBalance(accountFrom);
            accountDao.updateAccountBalance(accountTo);
        }
    }
}
