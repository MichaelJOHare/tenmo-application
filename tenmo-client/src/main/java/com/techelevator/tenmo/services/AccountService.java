package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.Account;
import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Balance;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class AccountService {
    private final String baseUrl;
    private final RestTemplate restTemplate = new RestTemplate();

    public AccountService(String url) {
        this.baseUrl = url;
    }

    public Balance getBalance(AuthenticatedUser authenticatedUser) {
        Balance balance = null;
        try {
            ResponseEntity<Balance> response = restTemplate.exchange(baseUrl + "balance", HttpMethod.GET,
                    makeEntity(authenticatedUser), Balance.class);
            balance = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return balance;
    }

    public Boolean updateAccount(AuthenticatedUser authenticatedUser, Transfer transfer, int currentUserAccountId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        boolean success = false;
        try {
            restTemplate.exchange(baseUrl + "/balance/account/" + currentUserAccountId, HttpMethod.PUT, entity, Transfer.class);
            success = true;
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return success;
    }

    public Account getAccountByUserId(AuthenticatedUser authenticatedUser, int userId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<Integer> entity = new HttpEntity<>(userId, headers);
        Account account = null;
        try {
            ResponseEntity<Account> response = restTemplate.exchange(baseUrl + "users/" + userId +
                            "/account", HttpMethod.GET,
                    entity, Account.class);
            account = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return account;
    }

    private HttpEntity<AuthenticatedUser> makeEntity(AuthenticatedUser authenticatedUser) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        return new HttpEntity<>(headers);
    }
}
