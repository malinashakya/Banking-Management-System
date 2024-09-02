package com.mycompany.bms.bean;

import com.mycompany.bms.model.Account;
import com.mycompany.bms.model.AccountStatusEnum;
import com.mycompany.bms.model.AccountTypeEnum;
import com.mycompany.bms.model.Transaction;
import com.mycompany.bms.model.TransactionTypeEnum;
import com.mycompany.bms.repository.AccountRepository;
import com.mycompany.bms.repository.TransactionRepository;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Bean for managing customer transactions, including transfers and PIN changes.
 */
@Named("transactionCustomerBean")
@ViewScoped
public class TransactionCustomerBean implements Serializable {

    private static final long serialVersionUID = 1L;

    @Inject
    private TransactionRepository transactionRepository;

    @Inject
    private AccountRepository accountRepository;

    @Inject
    private SessionCustomerBean sessionCustomerBean;

    @Inject
    private AccountInfoCustomerBean accountInfoCustomerBean;

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
    private boolean loggedIn;

    @PostConstruct
    public void init() {
        // Check if the user is logged in using SessionCustomerBean
        loggedIn = sessionCustomerBean.getCurrentCustomer() != null;

        if (loggedIn) {
            selectedEntity = new Transaction();
            // Initialize account list with only active savings accounts
            accountList = accountRepository.findAll().stream()
                    .filter(account -> account.getStatus() == AccountStatusEnum.ACTIVE
                    && account.getType() == AccountTypeEnum.SAVINGS)
                    .collect(Collectors.toList());
            loadOpeningBalance();
            loadClosingBalance();
            loadLatestTransactions();
        } else {
            // Redirect to login page if not logged in
            sessionCustomerBean.checkSession();
            // Initialize empty lists to avoid null pointers
            accountList = List.of();
            latestTransactions = List.of();
        }
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

    // Load the latest three transactions for the customer
    private void loadLatestTransactions() {
        Long loggedInCustomerId = getLoggedInCustomerId();
        latestTransactions = transactionRepository.getLatestTransactionByCustomer(loggedInCustomerId);
    }

    // Method to get logged-in customer ID
    private Long getLoggedInCustomerId() {
        return sessionCustomerBean.getCurrentCustomer() != null ? sessionCustomerBean.getCurrentCustomer().getId() : null;
    }

    public void saveOrUpdateEntity() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        try {
            Account sourceAccount = selectedEntity.getAccount();
            System.err.println("Source Account:" + sourceAccount);
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

                // Redirect to the PIN change page if 3 invalid attempts
                if (invalidPinCount >= 3) {
                    invalidPinCount = 0; // Reset the invalid PIN attempt counter
                    facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/Customer/PINChange.xhtml");
                    return;
                }
                return;
            }

            // Reset invalid pin count on correct entry
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

        Account sourceAccount = accountInfoCustomerBean.getCustomerSavingsAccounts().getFirst();
        System.err.println("Source Account2:" + sourceAccount);

        if (newPin == null || confirmNewPin == null || !newPin.equals(confirmNewPin)) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "PINs do not match or are empty"));
            return;
        }

        sourceAccount.setPin(newPin);
        accountRepository.update(sourceAccount);
        facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "PIN changed successfully"));
        invalidPinCount = 0; // Reset invalid PIN attempt counter
        showChangePinDialog = false; // Reset flag after PIN change

        // Redirect to home page or another appropriate page after successful PIN change
        try {
            facesContext.getExternalContext().redirect(facesContext.getExternalContext().getRequestContextPath() + "/Customer/CustomerDashboard.xhtml");
        } catch (IOException e) {
            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to redirect after PIN change."));
        }
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

    public int getInvalidPinCount() {
        return invalidPinCount;
    }

    public void setInvalidPinCount(int invalidPinCount) {
        this.invalidPinCount = invalidPinCount;
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

    public void setOpeningBalance(BigInteger openingBalance) {
        this.openingBalance = openingBalance;
    }

    public BigInteger getClosingBalance() {
        return closingBalance;
    }

    public void setClosingBalance(BigInteger closingBalance) {
        this.closingBalance = closingBalance;
    }

    public List<Transaction> getLatestTransactions() {
        return latestTransactions;
    }

    public void setLatestTransactions(List<Transaction> latestTransactions) {
        this.latestTransactions = latestTransactions;
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }
}
