package com.mycompany.bms.bean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "dashboardBean")
@ViewScoped
public class DashboardBean implements Serializable {
    private List<Transaction> transactions;
    private boolean loggedIn;

    public DashboardBean() {
        // Create an instance of PageAccessBean to handle login checks
        PageAccessBean pageAccessBean = new PageAccessBean();

        // Check if the user is logged in
        loggedIn = pageAccessBean.isLoggedIn();
        if (loggedIn) {
            // Initialize transactions if logged in
            transactions = new ArrayList<>();
            transactions.add(new Transaction("User1", 500));
            transactions.add(new Transaction("User2", 1000));
            transactions.add(new Transaction("User3", 2000));
        } else {
            // Redirect to the login page if not logged in
            pageAccessBean.checkLoginStatus();
            transactions = new ArrayList<>(); // Initialize an empty list to avoid null pointer issues
        }
    }

    public boolean isLoggedIn() {
        return loggedIn;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    // Inner class for transactions
    public static class Transaction {
        private String user;
        private int amount;

        public Transaction(String user, int amount) {
            this.user = user;
            this.amount = amount;
        }

        public String getUser() {
            return user;
        }

        public void setUser(String user) {
            this.user = user;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }
}
