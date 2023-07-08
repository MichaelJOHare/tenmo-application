package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcTransferDaoTests extends BaseDaoTests {

    private static final Transfer TRANSFER_1 = new Transfer(3001, 1, 1, 2001, 2002, new BigDecimal("100.00"));

    private JdbcTransferDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
    }

    @Test
    public void getTransferByIdShouldYieldCorrectTransfer() {
        Transfer actualTransfer = sut.getTransferById(3001);
        Assert.assertEquals(TRANSFER_1, actualTransfer);
    }
}
