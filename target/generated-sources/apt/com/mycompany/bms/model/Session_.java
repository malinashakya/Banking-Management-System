package com.mycompany.bms.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Session.class)
public abstract class Session_ {

	public static volatile SingularAttribute<Session, Date> logoutTime;
	public static volatile SingularAttribute<Session, Date> loginTime;
	public static volatile SingularAttribute<Session, Long> id;
	public static volatile SingularAttribute<Session, Customer> customer;

	public static final String LOGOUT_TIME = "logoutTime";
	public static final String LOGIN_TIME = "loginTime";
	public static final String ID = "id";
	public static final String CUSTOMER = "customer";

}

