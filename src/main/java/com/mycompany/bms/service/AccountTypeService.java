package com.mycompany.bms.service;

import com.mycompany.bms.model.AccountType;
import com.mycompany.bms.repository.AccountTypeRepository;
import javax.ejb.Stateless;
import javax.inject.Inject;

@Stateless
public class AccountTypeService extends GenericService<AccountType, Long> {

    // Inject the repository using field injection if necessary
    @Inject
    private AccountTypeRepository accountTypeRepository;

    public AccountTypeService() {
        // Call the super constructor with the repository
        super(null);  // Set to null or create a default repository instance if needed
    }

    @Inject
    public AccountTypeService(AccountTypeRepository accountTypeRepository) {
        super(accountTypeRepository);
        this.accountTypeRepository = accountTypeRepository;
    }

    // Additional methods specific to AccountType can be added here
}
