package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.dao.UserDao;
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
    private final UserDao userDao;
    public BalanceController(AccountDao accountDao, UserDao userDao) {
        this.accountDao = accountDao;
        this.userDao = userDao;
    }
    @RequestMapping(path = "", method = RequestMethod.GET)
    public Balance get(Principal principal) {
        String username = principal.getName();
        return accountDao.getBalance(username);
    }

}
