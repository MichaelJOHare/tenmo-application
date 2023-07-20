package com.techelevator.tenmo.services;


import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import com.techelevator.tenmo.model.UserCredentials;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ConsoleService {

    private final Scanner scanner = new Scanner(System.in);

    public int promptForMenuSelection(String prompt) {
        int menuSelection;
        System.out.print(prompt);
        try {
            menuSelection = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            menuSelection = -1;
        }
        return menuSelection;
    }

    public void printGreeting() {
        System.out.println("*********************");
        System.out.println("* Welcome to TEnmo! *");
        System.out.println("*********************");
    }

    public void printLoginMenu() {
        System.out.println();
        System.out.println("1: Register");
        System.out.println("2: Login");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printMainMenu() {
        System.out.println();
        System.out.println("1: View your current balance");
        System.out.println("2: View your past transfers");
        System.out.println("3: View your pending requests");
        System.out.println("4: Send TE bucks");
        System.out.println("5: Request TE bucks");
        System.out.println("0: Exit");
        System.out.println();
    }

    public void printSendRequestBanner() {
        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Users");
        System.out.println();
        System.out.println("ID\t\t\t\tUsername");
        System.out.println("-------------------------------------------");
    }

    public void printTransferHistoryBanner() {
        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Transfers");
        System.out.println();
        System.out.println("ID\t\t\t\tFrom/To\t\t\t\tAmount");
        System.out.println("-------------------------------------------");
    }

    public void printPendingTransfersBanner() {
        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Request Transfers Pending");
        System.out.println();
        System.out.println("ID\t\t\t\tFrom\t\t\t\tAmount");
        System.out.println("-------------------------------------------");
    }

    public void printTransferHistoryDetailsBanner() {
        System.out.println();
        System.out.println("-------------------------------------------");
        System.out.println("Transfer Details");
        System.out.println("-------------------------------------------");
    }

    public void printNotEnoughBalanceForTransfer() {
        System.out.println();
        System.out.println("**************************************************************");
        System.out.println("Your available balance is not high enough for the transaction.");
        System.out.println("**************************************************************");
    }

    public void printTransferDetails(TransferService transferService, UserService userService,
                                     AuthenticatedUser currentUser, Transfer transfer) {
        System.out.println();
        System.out.println("Id: " + transfer.getId());

        if (transfer.getTransferTypeId() == 2) {
            System.out.println("From: " + userService.getUsernameByAccountId(currentUser, transfer.getAccountFromId()));
        } else if (transfer.getTransferTypeId() == 1){
            System.out.println("From: " + userService.getUsernameByAccountId(currentUser, transfer.getAccountToId()));
        }

        if (transfer.getTransferTypeId() == 2) {
            System.out.println("To: " + userService.getUsernameByAccountId(currentUser, transfer.getAccountToId()));
        } else if (transfer.getTransferTypeId() == 1) {
            System.out.println("To: " + userService.getUsernameByAccountId(currentUser, transfer.getAccountFromId()));
        }

        System.out.println("Type: " + transferService.getTransferTypeDescriptionById(currentUser, transfer.getTransferTypeId()));
        System.out.println("Status: " + transferService.getTransferStatusDescriptionById(currentUser, transfer.getTransferStatusId()));
        System.out.println("Amount: $" + transfer.getAmount());
    }

    public void printPendingTransferDetails(TransferService transferService, UserService userService,
                                            AuthenticatedUser currentUser, Transfer transfer) {
        System.out.println();
        System.out.println("Id: " + transfer.getId());
        System.out.println("Transfer Request From: " + userService.getUsernameByAccountId(currentUser, transfer.getAccountToId()));
        System.out.println("Type: " + transferService.getTransferTypeDescriptionById(currentUser, transfer.getTransferTypeId()));
        System.out.println("Status: " + transferService.getTransferStatusDescriptionById(currentUser, transfer.getTransferStatusId()));
        System.out.println("Amount: $" + transfer.getAmount());
    }

    public void printApproveRejectChoiceMenu() {
        System.out.println();
        System.out.println("1: Approve");
        System.out.println("2: Reject");
        System.out.println("0: Don't approve or reject (cancel)");
    }

    public List<Transfer> createTransferList(List<Transfer> transferList, Transfer[] transfers,
                                             AccountService accountService, AuthenticatedUser currentUser) {
        for (Transfer transfer : transfers) {
            if (transfer.getAccountFromId() ==
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId() ||
                    transfer.getAccountToId() == accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId()) {
                transferList.add(transfer);
            }
        }
        return transferList;
    }

    public List<Transfer> createPendingRequestList(List<Transfer> requestList, Transfer[] transfers,
                                                   AccountService accountService, AuthenticatedUser currentUser) {
        for (Transfer transfer : transfers) {
            if (transfer.getAccountFromId() ==
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId() &&
            transfer.getTransferTypeId() == 1 && transfer.getTransferStatusId() == 1) {
                requestList.add(transfer);
            }
        }
        return requestList;
    }

    public void printCurrentUserTransferHistory(List<Transfer> transferList, AccountService accountService,
                                                UserService userService, AuthenticatedUser currentUser) {

        for (Transfer transfer : transferList) {
            if (transfer.getTransferTypeId() == 2 && transfer.getAccountFromId() ==
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId()) {
                System.out.println(transfer.getId() + "\t\t\tTo: " +
                        userService.getUsernameByAccountId(currentUser, transfer.getAccountToId()) + "\t\t\t$" +
                        transfer.getAmount());

            } else if (transfer.getTransferTypeId() == 2 && transfer.getAccountToId() ==
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId()) {
                System.out.println(transfer.getId() + "\t\t\tFrom: " +
                        userService.getUsernameByAccountId(currentUser, transfer.getAccountFromId()) + "\t\t\t$" +
                        transfer.getAmount());

            } else if (transfer.getTransferTypeId() == 1 && transfer.getAccountFromId() ==
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId() &&
                    transfer.getTransferStatusId() == 1) {
                System.out.println(transfer.getId() + "(Pending)\tRequest From: " +
                        userService.getUsernameByAccountId(currentUser, transfer.getAccountToId()) + "\t$" +
                        transfer.getAmount());

            } else if (transfer.getTransferTypeId() == 1 && transfer.getAccountToId() ==
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId() &&
                    transfer.getTransferStatusId() == 1) {
                System.out.println(transfer.getId() + "(Pending)\tRequest To: " +
                        userService.getUsernameByAccountId(currentUser, transfer.getAccountFromId()) + "\t$" +
                        transfer.getAmount());

            } else if (transfer.getTransferTypeId() == 1 && transfer.getAccountFromId() ==
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId() &&
                    transfer.getTransferStatusId() == 3) {
                System.out.println(transfer.getId() + "(Rejected)\tRequest From: " +
                        userService.getUsernameByAccountId(currentUser, transfer.getAccountToId()) + "\t$" +
                        transfer.getAmount());

            } else if (transfer.getTransferTypeId() == 1 && transfer.getAccountToId() ==
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId() &&
                    transfer.getTransferStatusId() == 3) {
                System.out.println(transfer.getId() + "(Rejected)\tRequest To: " +
                        userService.getUsernameByAccountId(currentUser, transfer.getAccountFromId()) + "\t$" +
                        transfer.getAmount());

            } else if (transfer.getTransferTypeId() == 1 && transfer.getAccountFromId() ==
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId() &&
                    transfer.getTransferStatusId() == 2) {
                System.out.println(transfer.getId() + "(Approved)\tRequest From: " +
                        userService.getUsernameByAccountId(currentUser, transfer.getAccountToId()) + "\t$" +
                        transfer.getAmount());

            } else if (transfer.getTransferTypeId() == 1 && transfer.getAccountToId() ==
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId() &&
                    transfer.getTransferStatusId() == 2) {
                System.out.println(transfer.getId() + "(Approved)\tRequest To: " +
                        userService.getUsernameByAccountId(currentUser, transfer.getAccountFromId()) + "\t$" +
                        transfer.getAmount());
            }
        }
    }

    public void printCurrentUserPendingRequests(List<Transfer> transferList, AccountService accountService,
                                                UserService userService, AuthenticatedUser currentUser) {
        for (Transfer transfer : transferList) {
            if (transfer.getTransferTypeId() == 1 && transfer.getAccountFromId() ==
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId()) {

                System.out.println(transfer.getId() + "\t\t\t" +
                        userService.getUsernameByAccountId(currentUser, transfer.getAccountToId()) + "\t\t\t\t$" +
                        transfer.getAmount());
            }
        }
    }

    public void printUserIdsAndNames(AuthenticatedUser authenticatedUser, User[] users) {
        for (User user : users) {
            if (user.getId() != authenticatedUser.getUser().getId()) {
                System.out.println(user.getId() + "\t\t\t" + user.getUsername());
            }
        }
    }

    public UserCredentials promptForCredentials() {
        String username = promptForString("Username: ");
        String password = promptForString("Password: ");
        return new UserCredentials(username, password);
    }

    public String promptForString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    public int promptForInt(String prompt) {
        System.out.println("-------------------------------------------");
        System.out.print(prompt);
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a number.");
            }
        }
    }

    public BigDecimal promptForBigDecimal(String prompt) {
        System.out.print(prompt);
        while (true) {
            try {
                return new BigDecimal(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a decimal number.");
            }
        }
    }

    public void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }

    public void printErrorMessage() {
        System.out.println("An error occurred. Check the log for details.");
    }

}
