package com.mycompany.bms.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Customer.class)
public abstract class Customer_ extends com.mycompany.bms.model.BaseEntity_ {

	public static volatile SingularAttribute<Customer, String> firstName;
	public static volatile SingularAttribute<Customer, String> lastName;
	public static volatile SingularAttribute<Customer, String> password;
	public static volatile SingularAttribute<Customer, String> address;
	public static volatile SingularAttribute<Customer, String> contact;
	public static volatile SingularAttribute<Customer, AccountType> accountType;
	public static volatile SingularAttribute<Customer, String> username;

	public static final String FIRST_NAME = "firstName";
	public static final String LAST_NAME = "lastName";
	public static final String PASSWORD = "password";
	public static final String ADDRESS = "address";
	public static final String CONTACT = "contact";
	public static final String ACCOUNT_TYPE = "accountType";
	public static final String USERNAME = "username";

}

