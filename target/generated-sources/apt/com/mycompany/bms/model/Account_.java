package com.mycompany.bms.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Account.class)
public abstract class Account_ extends com.mycompany.bms.model.BaseEntity_ {

	public static volatile SingularAttribute<Account, Float> balance;
	public static volatile SingularAttribute<Account, String> pin;
	public static volatile SingularAttribute<Account, AccountType> accountType;
	public static volatile SingularAttribute<Account, Float> interestEarned;
	public static volatile SingularAttribute<Account, String> accountNumber;
	public static volatile SingularAttribute<Account, Customer> customer;
	public static volatile SingularAttribute<Account, AccountStatusEnum> status;

	public static final String BALANCE = "balance";
	public static final String PIN = "pin";
	public static final String ACCOUNT_TYPE = "accountType";
	public static final String INTEREST_EARNED = "interestEarned";
	public static final String ACCOUNT_NUMBER = "accountNumber";
	public static final String CUSTOMER = "customer";
	public static final String STATUS = "status";

}

