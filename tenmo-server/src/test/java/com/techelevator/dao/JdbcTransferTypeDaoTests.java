package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferTypeDao;
import com.techelevator.tenmo.model.TransferType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTransferTypeDaoTests extends BaseDaoTests {

    private static final TransferType TRANSFER_TYPE_1 = new TransferType(1, "Request");
    private static final TransferType TRANSFER_TYPE_2 = new TransferType(2, "Send");

    private JdbcTransferTypeDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferTypeDao(jdbcTemplate);
    }

    @Test
    public void getTransferTypeByIdShouldYieldCorrectTransferType() {
        TransferType actualTransferType = sut.getTransferTypeById(1);
        Assert.assertEquals(TRANSFER_TYPE_1, actualTransferType);
    }
}
