package com.mycompany.bms.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Session_Customer.class)
public abstract class Session_ {

	public static volatile SingularAttribute<Session_Customer, Date> logoutTime;
	public static volatile SingularAttribute<Session_Customer, Date> loginTime;
	public static volatile SingularAttribute<Session_Customer, Long> id;
	public static volatile SingularAttribute<Session_Customer, Customer> customer;

	public static final String LOGOUT_TIME = "logoutTime";
	public static final String LOGIN_TIME = "loginTime";
	public static final String ID = "id";
	public static final String CUSTOMER = "customer";

}

