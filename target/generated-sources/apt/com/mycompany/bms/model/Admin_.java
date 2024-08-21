package com.mycompany.bms.model;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Admin.class)
public abstract class Admin_ extends com.mycompany.bms.model.BaseEntity_ {

	public static volatile SingularAttribute<Admin, String> password;
	public static volatile SingularAttribute<Admin, String> name;
	public static volatile SingularAttribute<Admin, String> username;

	public static final String PASSWORD = "password";
	public static final String NAME = "name";
	public static final String USERNAME = "username";

}

