package com.mycompany.bms.bean;

import com.mycompany.bms.model.*;
import com.mycompany.bms.repository.AccountRepository;
import com.mycompany.bms.repository.TransactionRepository;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named("transactionCustomerBean")
@ViewScoped
public class TransactionCustomerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TransactionRepository transactionRepository;

    @Inject
    private AccountRepository accountRepository;

    private Transaction selectedEntity;
    private String targetAccountNumber;
    private String targetAccountFullName;
    private BigInteger amount = BigInteger.ZERO;
    private List<Account> accountList;
    private String enteredPin;
    private int invalidPinCount = 0;
    private String newPin;
    private String confirmNewPin;
    private boolean showChangePinDialog;

    private BigInteger openingBalance = BigInteger.ZERO;
    private BigInteger closingBalance = BigInteger.ZERO;
    private List<Transaction> latestTransactions;

    @PostConstruct
    public void init() {
        selectedEntity = new Transaction();
        // Initialize account list with only active savings accounts
        accountList = accountRepository.findAll().stream()
                .filter(account -> account.getStatus() == AccountStatusEnum.ACTIVE
                        && account.getType() == AccountTypeEnum.SAVINGS)
                .collect(Collectors.toList());
        loadOpeningBalance();
        loadClosingBalance();
        loadLatestTransactions();
    }

    // Load Opening Balance at the start of the current month
    private void loadOpeningBalance() {
        Long loggedInCustomerId = getLoggedInCustomerId();
        try {
            openingBalance = transactionRepository.getOpeningBalanceForMonth(loggedInCustomerId);
        } catch (Exception e) {
            openingBalance = BigInteger.ZERO; // Fallback to zero if there's an issue
        }
    }

    // Load Closing Balance (current balance of the savings account)
    private void loadClosingBalance() {
        Long loggedInCustomerId = getLoggedInCustomerId();
        try {
            closingBalance = transactionRepository.getBalanceAsOfToday(loggedInCustomerId);
        } catch (Exception e) {
            closingBalance = BigInteger.ZERO; // Fallback to zero if there's an issue
        }
    }

    // Load the latest  three transactions for the customer
    private void loadLatestTransactions() {
        Long loggedInCustomerId = getLoggedInCustomerId();
        latestTransactions = transactionRepository.getLatestTransactionByCustomer(loggedInCustomerId);
    }

    // Mock method to get logged-in customer ID, replace with actual implementation
    private Long getLoggedInCustomerId() {
        // Retrieve the customer ID from the session or login context
        return 1L; // Replace with actual logic to get logged-in customer ID
    }

    public void saveOrUpdateEntity() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            Account sourceAccount = selectedEntity.getAccount();

            // Validate target account
            Optional<Account> optionalTargetAccount = accountRepository.findAll().stream()
                    .filter(account -> account.getType() == AccountTypeEnum.SAVINGS
                            && account.getStatus() == AccountStatusEnum.ACTIVE
                            && account.getAccountNumber().equals(targetAccountNumber)
                            && (account.getCustomer().getFirstName() + " " + account.getCustomer().getLastName()).equals(targetAccountFullName))
                    .findFirst();

            if (!optionalTargetAccount.isPresent()) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Target account number or full name does not match"));
                return;
            }

            Account targetAccount = optionalTargetAccount.get();

            // Check if source and target accounts are the same
            if (sourceAccount.getAccountNumber().equals(targetAccount.getAccountNumber())) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Transfer cannot be done to the same account"));
                return;
            }

            // Check for sufficient balance
            if (sourceAccount.getBalance().compareTo(amount) < 0) {
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Insufficient balance"));
                return;
            }

            // Validate entered PIN
            if (!sourceAccount.getPin().equals(enteredPin)) {
                invalidPinCount++;
                facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Invalid PIN. Attempt #" + invalidPinCount));

                // Show change PIN dialog if 3 invalid attempts
                if (invalidPinCount >= 3) {
                    showChangePinDialog = true;
                }
                return;
            }

            // Reset invalid PIN count and flag if correct
            invalidPinCount = 0;
            showChangePinDialog = false;

            // Perform the transfer
            sourceAccount.setBalance(sourceAccount.getBalance().subtract(amount));
            targetAccount.setBalance(targetAccount.getBalance().add(amount));

            // Save withdrawal transaction
            Transaction withdrawalTransaction = new Transaction();
            withdrawalTransaction.setAccount(sourceAccount);
            withdrawalTransaction.setTransactionType(TransactionTypeEnum.WITHDRAW);
            withdrawalTransaction.setAmount(amount);
            transactionRepository.save(withdrawalTransaction);

            // Save deposit transaction
            Transaction depositTransaction = new Transaction();
            depositTransaction.setAccount(targetAccount);
            depositTransaction.setTransactionType(TransactionTypeEnum.DEPOSIT);
            depositTransaction.setAmount(amount);
            transactionRepository.save(depositTransaction);

            // Update accounts
            accountRepository.update(sourceAccount);
            accountRepository.update(targetAccount);

            // Success message
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Transfer completed successfully"));

            // Reset form fields
            selectedEntity = new Transaction();
            enteredPin = "";

        } catch (Exception e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to complete transfer: " + e.getMessage()));
        }
    }

    // Method to handle the PIN change process
    public void changePin() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        Account sourceAccount = selectedEntity.getAccount();

        if (newPin == null || confirmNewPin == null || !newPin.equals(confirmNewPin)) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "PINs do not match or are empty"));
            return;
        }

        sourceAccount.setPin(newPin);
        accountRepository.update(sourceAccount);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "PIN changed successfully"));
        invalidPinCount = 0; // Reset invalid PIN attempt counter
        showChangePinDialog = false; // Reset flag after PIN change
    }

    // Method to prepare for viewing a transaction
    public void prepareViewEntity(Transaction transaction) {
        this.selectedEntity = transaction;
    }

    // Getters and setters
    public Transaction getSelectedEntity() {
        return selectedEntity;
    }

    public void setSelectedEntity(Transaction selectedEntity) {
        this.selectedEntity = selectedEntity;
    }

    public String getTargetAccountNumber() {
        return targetAccountNumber;
    }

    public void setTargetAccountNumber(String targetAccountNumber) {
        this.targetAccountNumber = targetAccountNumber;
    }

    public String getTargetAccountFullName() {
        return targetAccountFullName;
    }

    public void setTargetAccountFullName(String targetAccountFullName) {
        this.targetAccountFullName = targetAccountFullName;
    }

    public BigInteger getAmount() {
        return amount;
    }

    public void setAmount(BigInteger amount) {
        this.amount = amount;
    }

    public List<Account> getAccountList() {
        return accountList;
    }

    public void setAccountList(List<Account> accountList) {
        this.accountList = accountList;
    }

    public String getEnteredPin() {
        return enteredPin;
    }

    public void setEnteredPin(String enteredPin) {
        this.enteredPin = enteredPin;
    }

    public String getNewPin() {
        return newPin;
    }

    public void setNewPin(String newPin) {
        this.newPin = newPin;
    }

    public String getConfirmNewPin() {
        return confirmNewPin;
    }

    public void setConfirmNewPin(String confirmNewPin) {
        this.confirmNewPin = confirmNewPin;
    }

    public boolean isShowChangePinDialog() {
        return showChangePinDialog;
    }

    public void setShowChangePinDialog(boolean showChangePinDialog) {
        this.showChangePinDialog = showChangePinDialog;
    }

    public BigInteger getOpeningBalance() {
        return openingBalance;
    }

    public BigInteger getClosingBalance() {
        return closingBalance;
    }

    public List<Transaction> getLatestTransactions() {
        return latestTransactions;
    }
}
