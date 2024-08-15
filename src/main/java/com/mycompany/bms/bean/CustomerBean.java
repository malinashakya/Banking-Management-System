//package com.mycompany.bms.bean;
//
//import com.mycompany.bms.model.Customer;
//import com.mycompany.bms.model.AccountType;
//import com.mycompany.bms.repository.CustomerRepository;
//import javax.annotation.PostConstruct;
//import javax.ejb.EJB;
//import javax.faces.application.FacesMessage;
//import javax.faces.context.FacesContext;
//import javax.inject.Named;
//import javax.faces.view.ViewScoped;
//import java.io.Serializable;
//import java.util.List;
//
//@Named("customerBean")
//@ViewScoped
//public class CustomerBean implements Serializable {
//    private static final long serialVersionUID = 1L;
//
//    private Customer customer = new Customer();
//    private List<AccountType> accountTypes;
//
//    @EJB
//    private CustomerRepository customerRepository;
//
//    @PostConstruct
//    public void init() {
//        accountTypes = customerRepository.getAll(); // Assuming a method to get all account types
//    }
//
//    public Customer getCustomer() {
//        return customer;
//    }
//
//    public void setCustomer(Customer customer) {
//        this.customer = customer;
//    }
//
//    public List<AccountType> getAccountTypes() {
//        return accountTypes;
//    }
//
//    public void setAccountTypes(List<AccountType> accountTypes) {
//        this.accountTypes = accountTypes;
//    }
//
//    public void saveCustomer() {
//        FacesContext facesContext = FacesContext.getCurrentInstance();
//        try {
//            customerRepository.saveCustomer(customer);
//            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Success", "Customer saved successfully"));
//            customer = new Customer(); // Clear form after submission
//        } catch (Exception e) {
//            facesContext.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Error", "Failed to save customer"));
//            e.printStackTrace();
//        }
//    }
//}
