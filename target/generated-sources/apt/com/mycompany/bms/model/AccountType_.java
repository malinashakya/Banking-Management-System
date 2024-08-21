package com.mycompany.bms.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(AccountType.class)
public abstract class AccountType_ extends com.mycompany.bms.model.BaseEntity_ {

	public static volatile SingularAttribute<AccountType, Float> interestRate;
	public static volatile SingularAttribute<AccountType, AccountTypeEnum> accountType;
	public static volatile SingularAttribute<AccountType, Integer> timePeriod;

	public static final String INTEREST_RATE = "interestRate";
	public static final String ACCOUNT_TYPE = "accountType";
	public static final String TIME_PERIOD = "timePeriod";

}

