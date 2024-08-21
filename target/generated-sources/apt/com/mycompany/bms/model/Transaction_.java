package com.mycompany.bms.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transaction.class)
public abstract class Transaction_ {

	public static volatile SingularAttribute<Transaction, TransactionTypeEnum> transactionType;
	public static volatile SingularAttribute<Transaction, Date> date;
	public static volatile SingularAttribute<Transaction, Float> amount;
	public static volatile SingularAttribute<Transaction, Long> id;
	public static volatile SingularAttribute<Transaction, Date> transactionTime;
	public static volatile SingularAttribute<Transaction, Customer> customer;

	public static final String TRANSACTION_TYPE = "transactionType";
	public static final String DATE = "date";
	public static final String AMOUNT = "amount";
	public static final String ID = "id";
	public static final String TRANSACTION_TIME = "transactionTime";
	public static final String CUSTOMER = "customer";

}

