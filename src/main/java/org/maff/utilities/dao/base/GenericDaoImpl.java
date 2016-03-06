package org.maff.utilities.dao.base;

import java.io.Serializable;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;


public abstract class GenericDaoImpl<E,K extends Serializable> implements GenericDAO<E,K>{

	@Autowired
	private SessionFactory sessionFactory;
	
	protected Class<? extends E > daoType;
	
	/**
	 * According to this tutorial http://www.codesenior.com/en/tutorial/Spring-Generic-DAO-and-Generic-Service-Implementation
	 * By defining this class as abstract, we prevent Spring from creating 
	 * instance of this class If not defined as abstract, 
	 * getClass().getGenericSuperClass() would return Object. There would be 
	 * exception because Object class does not hava constructor with parameters.
	 */
	
	public GenericDaoImpl(){
		Type t = getClass().getGenericSuperclass();
		ParameterizedType pt = (ParameterizedType) t;
		daoType = (Class) pt.getActualTypeArguments()[0];
	}
	
	protected Session getCurrentSession(){
		return sessionFactory.getCurrentSession();
	}
	
	@Override
	public void add(E entity){
		getCurrentSession().save(entity);
	}
	
	@Override
	public void delete(E entity){
		getCurrentSession().delete(entity);
	}
	
	@Override
	public void update(E entity){
		getCurrentSession().update(entity);
	}
	
	@Override
	public E find(K key){
		
		return (E) getCurrentSession().get(daoType, key);
	}
}
