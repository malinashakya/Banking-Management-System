package com.mycompany.bms.bean;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@ManagedBean(name = "dashboardBean")
@ViewScoped
public class DashboardBean implements Serializable {
    private List<Transaction> transactions;

    public DashboardBean() {
        transactions = new ArrayList<>();
        transactions.add(new Transaction("User1", 500));
        transactions.add(new Transaction("User2", 1000));
        transactions.add(new Transaction("User3", 2000));
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
