package com.mycompany.bms.model;

import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Transaction.class)
public abstract class Transaction_ extends com.mycompany.bms.model.BaseEntity_ {

	public static volatile SingularAttribute<Transaction, TransactionTypeEnum> transactionType;
	public static volatile SingularAttribute<Transaction, LocalDate> date;
	public static volatile SingularAttribute<Transaction, BigInteger> amount;
	public static volatile SingularAttribute<Transaction, LocalDateTime> transactionTime;
	public static volatile SingularAttribute<Transaction, Account> account;

	public static final String TRANSACTION_TYPE = "transactionType";
	public static final String DATE = "date";
	public static final String AMOUNT = "amount";
	public static final String TRANSACTION_TIME = "transactionTime";
	public static final String ACCOUNT = "account";

}

