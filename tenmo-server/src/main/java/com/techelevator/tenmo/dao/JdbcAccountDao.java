package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.exception.DaoException;
import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.Balance;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.CannotGetJdbcConnectionException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{
    private final JdbcTemplate jdbcTemplate;
    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Balance getBalance(String username) {
        Balance balance = new Balance();
        String userBalance = "";
        String sql = "SELECT balance FROM account JOIN tenmo_user USING(user_id) WHERE username = ?;";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, username);

        try {
            if (results.next()) {
                userBalance = results.getString("balance");
            }
            if (userBalance != null) {
                balance.setBalance(new BigDecimal(userBalance));
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return balance;
    }

    @Override
    public Account getAccountById(int id) {
        Account account = null;
        String sql = "SELECT account_id, user_id, balance FROM account WHERE account_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
            return account;
        }  catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
    }

    @Override
    public void updateAccountBalance(Account account) {
        String sql = "UPDATE account SET balance = ? WHERE account_id = ?;";
        try {
            int rowsAffected = jdbcTemplate.update(sql, account.getBalance().getBalance(), account.getAccountId());
            if (rowsAffected != 1) {
                throw new DaoException(rowsAffected + " rows affected, expected 1.");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Cannot connect to server or database", e);
        } catch (DataIntegrityViolationException e) {
            throw new DaoException("Data integrity violation", e);
        }
    }

    @Override
    public Account getAccountByUserId(int userId) {
        Account account = new Account();
        String sql = "SELECT account_id, user_id, balance FROM account WHERE user_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, userId);
            if (results.next()) {
                account = mapRowToAccount(results);
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return account;
    }

    @Override
    public String getUsernameByAccountId(int accountId) {
        String username = "";
        String sql = "SELECT username from tenmo_user JOIN account USING(user_id) WHERE account_id = ?;";
        try {
            SqlRowSet results = jdbcTemplate.queryForRowSet(sql, accountId);
            if (results.next()) {
                username = results.getString("username");
            }
        } catch (CannotGetJdbcConnectionException e) {
            throw new DaoException("Unable to connect to server or database", e);
        }
        return username;
    }

    private Account mapRowToAccount(SqlRowSet rs) {
        int userId = rs.getInt("user_id");
        int accountId = rs.getInt("account_id");
        Balance balance = new Balance();
        String accountBalance = rs.getString("balance");
        balance.setBalance(new BigDecimal(accountBalance));
        return new Account(balance, accountId, userId);
    }
}
