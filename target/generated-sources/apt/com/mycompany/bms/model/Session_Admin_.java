package com.mycompany.bms.model;

import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Session_Admin.class)
public abstract class Session_Admin_ {

	public static volatile SingularAttribute<Session_Admin, Date> logoutTime;
	public static volatile SingularAttribute<Session_Admin, Date> loginTime;
	public static volatile SingularAttribute<Session_Admin, Admin> admin;
	public static volatile SingularAttribute<Session_Admin, Long> id;

	public static final String LOGOUT_TIME = "logoutTime";
	public static final String LOGIN_TIME = "loginTime";
	public static final String ADMIN = "admin";
	public static final String ID = "id";

}

