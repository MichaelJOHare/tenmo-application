package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferStatusDao;
import com.techelevator.tenmo.model.TransferStatus;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

public class JdbcTransferStatusDaoTests extends BaseDaoTests {

    private static final TransferStatus TRANSFER_STATUS_1 = new TransferStatus(1, "Pending");
    private static final TransferStatus TRANSFER_STATUS_2 = new TransferStatus(2, "Approved");
    private static final TransferStatus TRANSFER_STATUS_3 = new TransferStatus(3, "Rejected");
    private JdbcTransferStatusDao sut;
    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferStatusDao(jdbcTemplate);
    }

    @Test
    public void getTransferStatusByDescriptionShouldYieldCorrectTransferStatus() {
        TransferStatus actualTransferStatus = sut.getTransferStatusByDescription("Pending");
        Assert.assertEquals(TRANSFER_STATUS_1, actualTransferStatus);
    }
}
