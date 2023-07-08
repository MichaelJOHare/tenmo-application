package com.techelevator.tenmo;

import com.techelevator.tenmo.model.AuthenticatedUser;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.TransferType;
import com.techelevator.tenmo.model.UserCredentials;
import com.techelevator.tenmo.services.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class App {

    private static final String API_BASE_URL = "http://localhost:8080/";

    private final ConsoleService consoleService = new ConsoleService();
    private final AuthenticationService authenticationService = new AuthenticationService(API_BASE_URL);
    private final AccountService accountService = new AccountService(API_BASE_URL);
    private final UserService userService = new UserService(API_BASE_URL);
    private final TransferService transferService = new TransferService(API_BASE_URL);

    private AuthenticatedUser currentUser;

    public static void main(String[] args) {
        App app = new App();
        app.run();
    }

    private void run() {
        consoleService.printGreeting();
        loginMenu();
        if (currentUser != null) {
            mainMenu();
        }
    }
    private void loginMenu() {
        int menuSelection = -1;
        while (menuSelection != 0 && currentUser == null) {
            consoleService.printLoginMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                handleRegister();
            } else if (menuSelection == 2) {
                handleLogin();
            } else if (menuSelection != 0) {
                System.out.println("Invalid Selection");
                consoleService.pause();
            }
        }
    }

    private void handleRegister() {
        System.out.println("Please register a new user account");
        UserCredentials credentials = consoleService.promptForCredentials();
        if (authenticationService.register(credentials)) {
            System.out.println("Registration successful. You can now login.");
        } else {
            consoleService.printErrorMessage();
        }
    }

    private void handleLogin() {
        UserCredentials credentials = consoleService.promptForCredentials();
        currentUser = authenticationService.login(credentials);
        if (currentUser == null) {
            consoleService.printErrorMessage();
        }
    }

    private void mainMenu() {
        int menuSelection = -1;
        while (menuSelection != 0) {
            consoleService.printMainMenu();
            menuSelection = consoleService.promptForMenuSelection("Please choose an option: ");
            if (menuSelection == 1) {
                viewCurrentBalance();
            } else if (menuSelection == 2) {
                viewTransferHistory();
            } else if (menuSelection == 3) {
                viewPendingRequests();
            } else if (menuSelection == 4) {
                sendBucks();
            } else if (menuSelection == 5) {
                requestBucks();
            } else if (menuSelection == 0) {
                continue;
            } else {
                System.out.println("Invalid Selection");
            }
            consoleService.pause();
        }
    }

	private void viewCurrentBalance() {
        System.out.println("Your current account balance is: $" + accountService.getBalance(currentUser).getBalance());
	}

	private void viewTransferHistory() {
        consoleService.printTransferHistoryBanner();
        Transfer[] transfers = transferService.getTransfers(currentUser);
        List<Transfer> transferList = new ArrayList<>();
        transferList = consoleService.createTransferList(transferList, transfers, accountService, currentUser);
        if (transferList.size() > 0) {
            consoleService.printCurrentUserTransferHistory(transferList, accountService, userService, currentUser);
            int transferId = consoleService.promptForInt("Please enter transfer ID to view details (0 to cancel): ");
            if (transferId != 0) {
                Transfer transfer = transferService.getTransferById(currentUser, transferId);
                consoleService.printTransferHistoryDetailsBanner();
                consoleService.printTransferDetails(transferService, userService, currentUser, transfer);
            }
        } else {
            System.out.println("No transfer history available.");
        }
	}

	private void viewPendingRequests() {
        consoleService.printPendingTransfersBanner();
        Transfer[] transfers = transferService.getTransfers(currentUser);
        List<Transfer> pendingRequestList = new ArrayList<>();
        pendingRequestList = consoleService.createPendingRequestList(pendingRequestList, transfers, accountService, currentUser);
        if (pendingRequestList.size() > 0) {
            consoleService.printCurrentUserPendingRequests(pendingRequestList, accountService, userService, currentUser);
            int transferId = consoleService.promptForInt("Please enter transfer ID to approve/reject (0 to cancel): ");
            Transfer transfer = new Transfer();
            try {
                transfer = transferService.getTransferById(currentUser, transferId);
            } catch (NullPointerException e) {
                System.out.println("Invalid transfer ID entered.");
            }
            if (transferId != 0 && pendingRequestList.contains(transfer)) {
                consoleService.printPendingTransferDetails(transferService, userService, currentUser, transfer);
                consoleService.printApproveRejectChoiceMenu();
                int approveRejectChoice = consoleService.promptForInt("Please choose an option: ");
                if (approveRejectChoice != 0) {
                    if (approveRejectChoice == 1 && accountService.getBalance(currentUser).getBalance().subtract(transfer.getAmount()).compareTo(BigDecimal.ZERO) >= 0) {
                        transfer.setTransferStatusId(2);
                        transferService.updateTransfer(currentUser, transfer);
                        if (accountService.updateAccount(currentUser, transfer, accountService.getAccountByUserId(currentUser,
                                currentUser.getUser().getId()).getAccountId())) {
                            System.out.print("Request approved.");
                        } else {
                            System.out.println("Request approval failed.");
                        }
                    } else if (approveRejectChoice == 2) {
                        transfer.setTransferStatusId(3);
                        transferService.updateTransfer(currentUser, transfer);
                        if (transferService.getTransferById(currentUser, transfer.getId()).getTransferStatusId() == 3) {
                            System.out.println("Request rejected.");
                        } else {
                            System.out.println("Request rejection failed.");
                        }
                    } else {
                        consoleService.printNotEnoughBalanceForTransfer();
                    }
                }
            } else {
                System.out.println("Could not find pending request transfer with specified ID.");
            }
        } else {
            System.out.println("You have no pending transfers currently.");
        }
	}

	private void sendBucks() {
        consoleService.printSendRequestBanner();
        consoleService.printUserIdsAndNames(currentUser, userService.getUsers(currentUser));
        int receivingUserId = consoleService.promptForInt("Enter ID of user you are sending money to (0 to cancel): ");
        if (receivingUserId == currentUser.getUser().getId()) {
            System.out.println("You cannot send money to yourself.");
        }
        if (receivingUserId != 0 && receivingUserId != currentUser.getUser().getId()) {
            BigDecimal amountToSend = consoleService.promptForBigDecimal("Enter amount: $");
            if (accountService.getBalance(currentUser).getBalance().subtract(amountToSend).compareTo(BigDecimal.ZERO) >= 0) {
                int currentUserAccountId = accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId();
                int receivingUserAccountId = accountService.getAccountByUserId(currentUser, receivingUserId).getAccountId();
                Transfer sendTransfer = new Transfer(2, 2, currentUserAccountId,
                        receivingUserAccountId, amountToSend);
                Transfer sentTransfer = transferService.sendTransfer(currentUser, sendTransfer);
                if (accountService.updateAccount(currentUser, sentTransfer, currentUserAccountId)) {
                    System.out.println("Transfer complete.");
                } else {
                    System.out.println("Transfer failed.");
                }
            } else {
                consoleService.printNotEnoughBalanceForTransfer();
            }
        }
	}

	private void requestBucks() {
        consoleService.printSendRequestBanner();
        consoleService.printUserIdsAndNames(currentUser, userService.getUsers(currentUser));
        int requestingUserId = consoleService.promptForInt("Enter ID of user you are requesting money from (0 to cancel): ");
        if (requestingUserId == currentUser.getUser().getId()) {
            System.out.println("You cannot request money from yourself.");
        }
        if (requestingUserId != 0 && requestingUserId != currentUser.getUser().getId()) {
            BigDecimal amountToRequest = consoleService.promptForBigDecimal("Enter amount: $");
            Transfer requestTransfer = new Transfer(1, 1,
                    accountService.getAccountByUserId(currentUser, requestingUserId).getAccountId(),
                    accountService.getAccountByUserId(currentUser, currentUser.getUser().getId()).getAccountId(),
                    amountToRequest);
            Transfer requestedTransfer = transferService.sendTransfer(currentUser, requestTransfer);
            if (transferService.getTransferStatusDescriptionById(currentUser,
                    requestedTransfer.getTransferStatusId()).equals("Pending")) {
                System.out.println("Request was sent and is pending approval from user.");
            } else {
                System.out.println("Request failed.");
            }
        }
	}
}


//to do: how to implement approve/reject for requestBucks
