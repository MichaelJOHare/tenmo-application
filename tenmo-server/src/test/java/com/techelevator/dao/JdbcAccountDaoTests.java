package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests {
    protected static final Balance TEST_BALANCE = new Balance();
    protected static final Account ACCOUNT_1 = new Account(TEST_BALANCE, 2001, 1001);
    protected static final Account ACCOUNT_2 = new Account(TEST_BALANCE, 2002, 1002);
    protected static final Account ACCOUNT_3 = new Account(TEST_BALANCE, 2003, 1003);

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void getAccountByIdShouldReturnCorrectAccount() {
        TEST_BALANCE.setBalance(new BigDecimal("100.00"));
        Account actualAccount = sut.getAccountById(ACCOUNT_1.getAccountId());
        Assert.assertEquals(ACCOUNT_1, actualAccount);
    }
}
