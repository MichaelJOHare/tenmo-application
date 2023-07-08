package com.techelevator.tenmo.services;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.util.BasicLogger;
import org.springframework.http.*;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

public class TransferService {
    private final String baseurl;
    private final RestTemplate restTemplate = new RestTemplate();

    public TransferService(String url) {
        this.baseurl = url;
    }

    public Transfer sendTransfer(AuthenticatedUser authenticatedUser, Transfer newTransfer) {
        HttpEntity<Transfer> entity = makeEntity(authenticatedUser, newTransfer);
        Transfer newSendTransfer = null;
        try {
            newSendTransfer = restTemplate.postForObject(baseurl + "transfers", entity, Transfer.class);
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return newSendTransfer;
    }

    public Transfer[] getTransfers(AuthenticatedUser authenticatedUser) {
        Transfer[] transfers = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        HttpEntity<AuthenticatedUser> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<Transfer[]> response = restTemplate.exchange(baseurl + "transfers",
                    HttpMethod.GET, entity, Transfer[].class);
            transfers = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfers;
    }

    public Transfer getTransferById(AuthenticatedUser authenticatedUser, int id) {
        Transfer transfer = null;
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Integer> entity = new HttpEntity<>(id, headers);
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(baseurl + "transfers/transfer/" + id, HttpMethod.GET,
                    entity, Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    public String getTransferTypeDescriptionById(AuthenticatedUser authenticatedUser, int id) {
        String transferTypeDescription = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Integer> entity = new HttpEntity<>(id, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(baseurl + "transfers/transfer_type/" + id, HttpMethod.GET,
                    entity, String.class);
            transferTypeDescription = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferTypeDescription;
    }

    public String getTransferStatusDescriptionById(AuthenticatedUser authenticatedUser, int id) {
        String transferStatusDescription = "";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Integer> entity = new HttpEntity<>(id, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(baseurl + "transfers/transfer_status/" + id, HttpMethod.GET,
                    entity, String.class);
            transferStatusDescription = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transferStatusDescription;
    }

    public Transfer updateTransfer(AuthenticatedUser authenticatedUser, Transfer transfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Transfer> entity = new HttpEntity<>(transfer, headers);
        try {
            ResponseEntity<Transfer> response = restTemplate.exchange(baseurl + "transfers/" + transfer.getId(),
                    HttpMethod.PUT, entity, Transfer.class);
            transfer = response.getBody();
        } catch (RestClientResponseException | ResourceAccessException e) {
            BasicLogger.log(e.getMessage());
        }
        return transfer;
    }

    private HttpEntity<Transfer> makeEntity(AuthenticatedUser authenticatedUser, Transfer newTransfer) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(authenticatedUser.getToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(newTransfer, headers);
    }
}
