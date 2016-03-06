package org.maff.utilities.dao.base;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateUtil {

	public static SessionFactory sessionFactory;
	
	private HibernateUtil(){
		
	}
	
	public static synchronized SessionFactory getSessionFactory(){
		
		if (sessionFactory == null){
			sessionFactory = new Configuration().configure("spring4.xml").buildSessionFactory();
		}
		return sessionFactory;
	}
}
