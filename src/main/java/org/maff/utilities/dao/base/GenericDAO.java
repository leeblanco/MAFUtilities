package org.maff.utilities.dao.base;

public interface GenericDAO<E,K> {

	public void add(E entity);
	public void delete(E entity);
	public void update(E entity);
	public E find(K key);
	
}
