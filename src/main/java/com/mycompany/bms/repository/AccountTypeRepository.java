/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.bms.repository;

import com.mycompany.bms.model.AccountType;
import javax.ejb.Stateless;

@Stateless
public class AccountTypeRepository extends GenericRepository<AccountType, Long> {

    public AccountTypeRepository() {
        super(AccountType.class);
    }
}
